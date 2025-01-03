import React, { useState } from 'react';
import { Card, Descriptions, Button, Form, Input, Modal, Row, Col, Select, InputNumber, message } from 'antd';
import styles from './styles/RestaurantInfo.module.css';
import { updateRestaurant } from '../../services/restaurantApis';
import { useAuth } from '../../store/AuthContext';
import { LEAD_STATUS } from '../../constants/leadStatusContstants';

const { Option } = Select;


interface RestaurantInfoProps {
  restaurant: {
    id: number;
    name: string;
    address: string;
    city: string;
    state: string;
    phone: string;
    email: string;
    leadStatus: string;
    annualRevenue: number;
    timezone: string;
  };
}

const RestaurantInfo: React.FC<RestaurantInfoProps> = ({ restaurant }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const {token} = useAuth();
  const [localRestaurant, setLocalRestaurant] = useState(restaurant);

  const handleEdit = () => {
    form.setFieldsValue(localRestaurant);
    setIsEditing(true);
  };

  const handleCancel = () => {
    setIsEditing(false);
    form.resetFields();
  };

  const handleSave = async (values: any) => {
    setLoading(true);
    try {
      let res = await updateRestaurant(restaurant.id, values, token || "");
      setLocalRestaurant(res);
      message.success('Restaurant information updated successfully');
      setIsEditing(false);
    } catch (error) {
      message.error(JSON.stringify(error?.response?.data?.details) || JSON.stringify(error?.response?.data?.message) || 'Failed to update restaurant information');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <Card
        title="Restaurant Information"
        extra={<Button type="primary" onClick={handleEdit}>Edit</Button>}
        className={styles.card}
      >
        <Descriptions column={{ xs: 1, sm: 2, md: 3 }} bordered>
          <Descriptions.Item label="Name">{localRestaurant.name}</Descriptions.Item>
          <Descriptions.Item label="Address">{localRestaurant.address}</Descriptions.Item>
          <Descriptions.Item label="City">{localRestaurant.city}</Descriptions.Item>
          <Descriptions.Item label="State">{localRestaurant.state}</Descriptions.Item>
          <Descriptions.Item label="Phone">{localRestaurant.phone}</Descriptions.Item>
          <Descriptions.Item label="Email">{localRestaurant.email}</Descriptions.Item>
          <Descriptions.Item label="Lead Status">{localRestaurant.leadStatus}</Descriptions.Item>
          <Descriptions.Item label="Annual Revenue">
            ${localRestaurant.annualRevenue.toFixed(2)}
          </Descriptions.Item>
          <Descriptions.Item label="Timezone">{localRestaurant.timezone}</Descriptions.Item>
        </Descriptions>
      </Card>

      <Modal
        title="Edit Restaurant Information"
        open={isEditing}
        onCancel={handleCancel}
        footer={null}
        width={700}
        centered
        destroyOnClose
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSave}
          initialValues={localRestaurant}
        >
          <Row gutter={24}>
            <Col xs={24} sm={12}>
              <Form.Item
                label="Restaurant Name"
                name="name"
                rules={[{ required: true, message: 'Please input the restaurant name!' }]}
              >
                <Input placeholder="Enter restaurant name" />
              </Form.Item>

              <Form.Item
                label="Address"
                name="address"
                rules={[{ required: true, message: 'Please input the address!' }]}
              >
                <Input placeholder="Enter address" />
              </Form.Item>

              <Form.Item
                label="City"
                name="city"
                rules={[{ required: true, message: 'Please input the city!' }]}
              >
                <Input placeholder="Enter city" />
              </Form.Item>

              <Form.Item
                label="Phone"
                name="phone"
                rules={[{ required: true, message: 'Please input the phone number!' }]}
              >
                <Input placeholder="Enter phone number" />
              </Form.Item>

              <Form.Item
                label="Timezone"
                name="timezone"
                rules={[{ required: true, message: 'Please input the timezone!' }]}
              >
                <Input placeholder="Enter timezone" />
              </Form.Item>
            </Col>

            <Col xs={24} sm={12}>
              <Form.Item
                label="State"
                name="state"
                rules={[{ required: true, message: 'Please input the state!' }]}
              >
                <Input placeholder="Enter state" />
              </Form.Item>

              <Form.Item
                label="Annual Revenue"
                name="annualRevenue"
                rules={[{ required: true, message: 'Please input the annual revenue!' }]}
              >
                <InputNumber
                  style={{ width: '100%' }}
                  min={0}
                  step={0.01}
                  placeholder="Enter revenue"
                />
              </Form.Item>

              <Form.Item
                label="Lead Status"
                name="leadStatus"
                rules={[{ required: true, message: 'Please select the lead status!' }]}
              >
                <Select placeholder="Select status">
                  {Object.values(LEAD_STATUS).map((status) => (
                    <Option key={status} value={status}>
                      {status}
                    </Option>
                  ))}
                </Select>
              </Form.Item>

              <Form.Item
                label="Email"
                name="email"
                rules={[
                  { required: true, type: 'email', message: 'Please input a valid email!' }
                ]}
              >
                <Input placeholder="Enter email" />
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
                Save Changes
              </Button>
            </Col>
          </Row>
        </Form>
      </Modal>
    </div>
  );
};

export default RestaurantInfo;