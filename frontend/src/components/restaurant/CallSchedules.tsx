import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, InputNumber, DatePicker, Space, Row, Col, message } from 'antd';
import { EditOutlined, DeleteOutlined, PhoneOutlined } from '@ant-design/icons';
import styles from './styles/CallSchedules.module.css';
import dayjs from 'dayjs';
import { createCallSchedule, deleteCallSchedule, updateCallSchedule } from '../../services/callScheduleApis';
import { useAuth } from '../../store/AuthContext';

interface CallSchedule {
  id: number;
  frequencyDays: number;
  lastCallDate: string;
  nextCallDate: string;
  priorityLevel: number;
  createdAt: string;
}

interface CallSchedulesProps {
  schedules: CallSchedule[];
  restaurantId: number;
}

const CallSchedules: React.FC<CallSchedulesProps> = ({ schedules: initialSchedules, restaurantId }) => {
  const [schedules, setSchedules] = useState<CallSchedule[]>(initialSchedules);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingSchedule, setEditingSchedule] = useState<CallSchedule | null>(null);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const { token } = useAuth();

  useEffect(() => {
    setSchedules(initialSchedules); // Sync state with the prop if it changes
  }, [initialSchedules]);

  const handleAdd = () => {
    setEditingSchedule(null);
    form.resetFields();
    setIsModalVisible(true);
  };

  const handleEdit = (schedule: CallSchedule) => {
    setEditingSchedule(schedule);
    form.setFieldsValue({
      ...schedule,
      lastCallDate: dayjs(schedule.lastCallDate),
      nextCallDate: dayjs(schedule.nextCallDate),
    });

    setIsModalVisible(true);
  };

  const handleDelete = async (scheduleId: number) => {
    try {
      await deleteCallSchedule(scheduleId, token || '', restaurantId);
      setSchedules(schedules.filter((schedule) => schedule.id !== scheduleId)); // Update state locally
      message.success('Schedule deleted successfully');
    } catch (error) {
       message.error(JSON.stringify(error?.response?.data?.details) || JSON.stringify(error?.response?.data?.message) || 'Failed to delete schedule');
    }
  };

  const handleSubmit = async (values: any) => {
    setLoading(true);
    try {
      const formattedValues = {
        ...values,
        lastCallDate: values.lastCallDate.toISOString().slice(0, 19), // Extract only the first 19 characters
        nextCallDate: values.nextCallDate.toISOString().slice(0, 19), // Extract only the first 19 characters
      };
      

      if (editingSchedule) {
        await updateCallSchedule(editingSchedule.id, formattedValues, token || '', restaurantId);
        setSchedules(
          schedules.map((schedule) =>
            schedule.id === editingSchedule.id ? { ...schedule, ...formattedValues } : schedule
          )
        );
        message.success('Schedule updated successfully');
      } else {
        const newSchedule = await createCallSchedule(formattedValues, token || '', restaurantId);
        setSchedules([...schedules, newSchedule]);
        message.success('Schedule created successfully');
      }
      setIsModalVisible(false);
    } catch (error) {
       message.error(JSON.stringify(error?.response?.data?.details) || JSON.stringify(error?.response?.data?.message) || 'Failed to save schedule');
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    {
      title: 'Frequency (Days)',
      dataIndex: 'frequencyDays',
      key: 'frequencyDays',
    },
    {
      title: 'Last Call Date',
      dataIndex: 'lastCallDate',
      key: 'lastCallDate',
      render: (date: string) => new Date(date).toLocaleString(),
    },
    {
      title: 'Next Call Date',
      dataIndex: 'nextCallDate',
      key: 'nextCallDate',
      render: (date: string) => new Date(date).toLocaleString(),
    },
    {
      title: 'Priority Level',
      dataIndex: 'priorityLevel',
      key: 'priorityLevel',
      render: (level: number) => {
        const color = level === 1 ? 'red' : level === 2 ? 'orange' : 'green';
        return <span style={{ color }}>{level}</span>;
      },
    },
    {
      title: 'Created At',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (date: string) => new Date(date).toLocaleDateString(),
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (text: string, record: CallSchedule) => (
        <Space size="middle">
          <Button
            icon={<EditOutlined />}
            onClick={() => handleEdit(record)}
          />
          <Button
            danger
            icon={<DeleteOutlined />}
            onClick={() => handleDelete(record.id)}
          />
        </Space>
      ),
    },
  ];

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <Button
          type="primary"
          icon={<PhoneOutlined />}
          onClick={handleAdd}
          className={styles.addButton}
        >
          Add Call Schedule
        </Button>
      </div>

      <Table
        columns={columns}
        dataSource={schedules}
        rowKey="id"
        pagination={{ pageSize: 10 }}
        style={{ minWidth: '800px' }}
      />

      <Modal
        title={editingSchedule ? 'Edit Call Schedule' : 'Add New Call Schedule'}
        open={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        footer={null}
        width={700}
        centered
        destroyOnClose
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
        >
          <Row gutter={24}>
            <Col xs={24} sm={12}>
              <Form.Item
                label="Frequency (Days)"
                name="frequencyDays"
                rules={[{ required: true, message: 'Please input the frequency!' }]}
              >
                <InputNumber
                  min={1}
                  style={{ width: '100%' }}
                  placeholder="Enter frequency in days"
                />
              </Form.Item>

              <Form.Item
                label="Last Call Date"
                name="lastCallDate"
                rules={[{ required: true, message: 'Please select the last call date!' }]}
              >
                <DatePicker
                  showTime
                  style={{ width: '100%' }}
                  format="YYYY-MM-DD HH:mm:ss"
                />
              </Form.Item>
            </Col>

            <Col xs={24} sm={12}>
              <Form.Item
                label="Next Call Date"
                name="nextCallDate"
                rules={[{ required: true, message: 'Please select the next call date!' }]}
              >
                <DatePicker
                  showTime
                  style={{ width: '100%' }}
                  format="YYYY-MM-DD HH:mm:ss"
                />
              </Form.Item>

              <Form.Item
                label="Priority Level"
                name="priorityLevel"
                rules={[{ required: true, message: 'Please input the priority level!' }]}
              >
                <InputNumber
                  min={1}
                  max={3}
                  style={{ width: '100%' }}
                  placeholder="Enter priority level (1-3)"
                />
              </Form.Item>
            </Col>
          </Row>

          <Row>
            <Col span={24}>
              <Button
                type="primary"
                htmlType="submit"
                loading={loading}
                block
                size="large"
              >
                {editingSchedule ? 'Save Changes' : 'Add Schedule'}
              </Button>
            </Col>
          </Row>
        </Form>
      </Modal>
    </div>
  );
};

export default CallSchedules;
