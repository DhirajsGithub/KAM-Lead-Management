import React, { useState } from "react";
import {
  Table,
  Button,
  Modal,
  Form,
  Input,
  Select,
  Space,
  Row,
  message,
} from "antd";
import { PlusOutlined, EditOutlined, DeleteOutlined } from "@ant-design/icons";
import dayjs from "dayjs";
import styles from "./styles/Orders.module.css";
import { useAuth } from "../../store/AuthContext";
import {
  createOrder,
  deleteOrder,
  updateOrder,
} from "../../services/orderApis";
import ordersStatus from "../../constants/ordersStatus";

const Orders = ({ restaurantId, orders: initialOrders }) => {
  const [orders, setOrders] = useState(initialOrders);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingOrder, setEditingOrder] = useState(null);
  const { token } = useAuth();
  const [form] = Form.useForm();

  const handleAdd = async (values) => {
    const data = {
      ...values,
      orderDate: new Date().toISOString().slice(0, 19),
    };
    try {
      const newOrder = await createOrder(data, token || "", restaurantId);
      message.success("Order added successfully");

      // Update local state
      setOrders((prevOrders) => [...prevOrders, newOrder]);
    } catch (error) {
      message.error(
        JSON.stringify(error?.response?.data?.details) ||
          JSON.stringify(error?.response?.data?.message) ||
          "Failed to add Order"
      );
    }
    setIsModalVisible(false);
    form.resetFields();
  };

  const handleEdit = async (values) => {
    const data = {
      ...values,
      orderDate: new Date(editingOrder.orderDate).toISOString().slice(0, 19),
    };
    try {
      const updatedOrder = await updateOrder(
        editingOrder.id,
        data,
        token || "",
        restaurantId
      );
      message.success("Order updated successfully");

      // Update local state
      setOrders((prevOrders) =>
        prevOrders.map((order) =>
          order.id === editingOrder.id ? updatedOrder : order
        )
      );
    } catch (error) {
      message.error(
        JSON.stringify(error?.response?.data?.details) ||
          JSON.stringify(error?.response?.data?.message) ||
          "Failed to update Order"
      );
    }
    setIsModalVisible(false);
    setEditingOrder(null);
    form.resetFields();
  };

  const handleDelete = async (orderId) => {
    try {
      await deleteOrder(orderId, token || "", restaurantId);
      message.success("Order deleted successfully");

      // Update local state
      setOrders((prevOrders) =>
        prevOrders.filter((order) => order.id !== orderId)
      );
    } catch (error) {
      message.error(
        JSON.stringify(error?.response?.data?.details) ||
          JSON.stringify(error?.response?.data?.message) ||
          "Failed to delete Order"
      );
    }
  };

  const columns = [
    {
      title: "Order ID",
      dataIndex: "id",
      key: "id",
    },
    {
      title: "Order Date",
      dataIndex: "orderDate",
      key: "orderDate",
      render: (date) => dayjs(date).format("MMM DD, YYYY HH:mm"),
    },
    {
      title: "Total Amount",
      dataIndex: "totalAmount",
      key: "totalAmount",
      render: (amount) => `$${amount.toFixed(2)}`,
    },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
    },
    {
      title: "Actions",
      key: "actions",
      render: (_, record) => (
        <Space>
          <Button
            icon={<EditOutlined />}
            onClick={() => {
              setEditingOrder(record);
              form.setFieldsValue(record);
              setIsModalVisible(true);
            }}
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
      <Row justify="end">
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => setIsModalVisible(true)}
          className={styles.addButton}
        >
          Add Order
        </Button>
      </Row>

      <Table
        style={{ minWidth: "800px", overflowX: "scroll" }}
        columns={columns}
        dataSource={orders}
        rowKey="id"
      />

      <Modal
        title={editingOrder ? "Edit Order" : "Add Order"}
        open={isModalVisible}
        onCancel={() => {
          setIsModalVisible(false);
          setEditingOrder(null);
          form.resetFields();
        }}
        footer={null}
      >
        <Form
          form={form}
          onFinish={editingOrder ? handleEdit : handleAdd}
          layout="vertical"
        >
          <Form.Item
            name="totalAmount"
            label="Total Amount"
            rules={[{ required: true }]}
          >
            <Input type="number" />
          </Form.Item>
          <Form.Item name="status" label="Status" rules={[{ required: true }]}>
            <Select>
              {ordersStatus.map((status) => (
                <Select.Option key={status.id} value={status.name}>
                  {status.name}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              {editingOrder ? "Update" : "Add"}
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default Orders;
