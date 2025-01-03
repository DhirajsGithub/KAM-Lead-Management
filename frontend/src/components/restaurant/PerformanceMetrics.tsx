import React, { useState } from 'react';
import { Card, Row, Col, Statistic, Button, Modal, Form, Input, DatePicker, Space, message } from 'antd';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { PlusOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import styles from './styles/Performance.module.css';
import { useAuth } from '../../store/AuthContext';
import { createMetric } from '../../services/performanceMetriceApis';

interface Metric {
  id: number;
  metricDate: string;
  leadsCount: number;
  closedDeals: number;
  revenue: number;
  followUpsCount: number;
  createdAt: string;
}

interface Props {
  restaurantId: number;
  metrics: Metric[];
}

const PerformanceMetrics: React.FC<Props> = ({ restaurantId, metrics: initialMetrics }) => {
  const [metrics, setMetrics] = useState<Metric[]>(initialMetrics);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const { token } = useAuth();
  const [form] = Form.useForm();

  const handleAdd = async (values: any) => {
    try {
      const newMetric = await createMetric({
        ...values,
        metricDate: values.metricDate.toISOString().slice(0, 19),
      }, token || "", restaurantId);

      // Add the new metric to the local state
      setMetrics((prevMetrics) => [...prevMetrics, newMetric]);

      message.success('Metric added successfully!');
      setIsModalVisible(false);
      form.resetFields();
    } catch (error) {
      console.error('Error creating metric:', error);
       message.error(JSON.stringify(error?.response?.data?.details) || JSON.stringify(error?.response?.data?.message) || 'Failed to add metric. Please try again.');
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2>Performance Metrics</h2>
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => setIsModalVisible(true)}
          className={styles.addButton}
        >
          Add Metric
        </Button>
      </div>

      <Row gutter={[16, 16]} className={styles.statsRow}>
        <StatCard title="Leads" metrics={metrics} dataKey="leadsCount" />
        <StatCard title="Closed Deals" metrics={metrics} dataKey="closedDeals" />
        <StatCard title="Revenue" metrics={metrics} dataKey="revenue" prefix="$" />
        <StatCard title="Follow-ups" metrics={metrics} dataKey="followUpsCount" />
      </Row>

      <Card className={styles.chartCard} title="Metrics Trend">
        <ResponsiveContainer width="100%" height={400}>
          <LineChart data={metrics}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis
              dataKey="metricDate"
              tickFormatter={(date) => dayjs(date).format('MMM DD')}
            />
            <YAxis />
            <Tooltip
              labelFormatter={(date) => dayjs(date).format('MMM DD, YYYY')}
              formatter={(value: number, name: string) => {
                if (name === 'revenue') return ['$' + value.toFixed(2), 'Revenue'];
                return [value, name.replace(/([A-Z])/g, ' $1').trim()];
              }}
            />
            <Line
              type="monotone"
              dataKey="revenue"
              stroke="#1890ff"
              name="Revenue"
              dot={true}
            />
            <Line
              type="monotone"
              dataKey="leadsCount"
              stroke="#52c41a"
              name="Leads Count"
              dot={true}
            />
            <Line
              type="monotone"
              dataKey="closedDeals"
              stroke="#722ed1"
              name="Closed Deals"
              dot={true}
            />
            <Line
              type="monotone"
              dataKey="followUpsCount"
              stroke="#fa8c16"
              name="Follow Ups"
              dot={true}
            />
          </LineChart>
        </ResponsiveContainer>
      </Card>

      <Modal
        title="Add Metric"
        open={isModalVisible}
        onCancel={() => {
          setIsModalVisible(false);
          form.resetFields();
        }}
        footer={null}
      >
        <Form
          form={form}
          onFinish={handleAdd}
          layout="vertical"
          initialValues={{ metricDate: dayjs() }}
        >
          <Form.Item
            name="metricDate"
            label="Metric Date"
            rules={[{ required: true, message: 'Please select a date' }]}
          >
            <DatePicker className={styles.datePicker} showTime />
          </Form.Item>

          <Form.Item
            name="leadsCount"
            label="Leads Count"
            rules={[{ required: true, message: 'Please enter leads count' }]}
          >
            <Input type="number" min={0} />
          </Form.Item>

          <Form.Item
            name="closedDeals"
            label="Closed Deals"
            rules={[{ required: true, message: 'Please enter closed deals' }]}
          >
            <Input type="number" min={0} />
          </Form.Item>

          <Form.Item
            name="revenue"
            label="Revenue"
            rules={[{ required: true, message: 'Please enter revenue' }]}
          >
            <Input type="number" min={0} step="0.01" prefix="$" />
          </Form.Item>

          <Form.Item
            name="followUpsCount"
            label="Follow-ups Count"
            rules={[{ required: true, message: 'Please enter follow-ups count' }]}
          >
            <Input type="number" min={0} />
          </Form.Item>

          <Form.Item className={styles.modalButtons}>
            <Space>
              <Button onClick={() => {
                setIsModalVisible(false);
                form.resetFields();
              }}>
                Cancel
              </Button>
              <Button type="primary" htmlType="submit">
                Add
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

const StatCard = ({
  title,
  metrics,
  dataKey,
  prefix,
}: {
  title: string;
  metrics: Metric[];
  dataKey: keyof Metric;
  prefix?: string;
}) => {
  const calculateMetricStats = (metrics: Metric[], key: keyof Metric) => {
    if (metrics.length < 2) return { current: metrics[0]?.[key] || 0, previous: 0, percentChange: 0 };

    const current = metrics[metrics.length - 1][key] as number;
    const previous = metrics[metrics.length - 2][key] as number;
    const percentChange = ((current - previous) / previous) * 100;
    return { current, previous, percentChange };
  };

  const stats = calculateMetricStats(metrics, dataKey);
  const isPositive = stats.percentChange >= 0;

  return (
    <Col xs={24} sm={12} lg={6}>
      <Card className={styles.statCard}>
        <Statistic title={title} value={stats.current} prefix={prefix} />
        <div className={styles.trendInfo}>
          <span className={isPositive ? styles.positive : styles.negative}>
            {isPositive ? '↑' : '↓'} {Math.abs(stats.percentChange).toFixed(1)}%
          </span>
          <span className={styles.previous}>Previous: {prefix}{stats.previous}</span>
        </div>
      </Card>
    </Col>
  );
};

export default PerformanceMetrics;
