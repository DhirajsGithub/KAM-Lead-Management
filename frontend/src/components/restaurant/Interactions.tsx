import React, { useEffect, useState } from "react";
import {
  Card,
  Button,
  Modal,
  Form,
  Input,
  DatePicker,
  Select,
  Space,
  Typography,
  Row,
  Col,
  message,
} from "antd";
import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import dayjs from "dayjs";
import styles from "./styles/Interactions.module.css";
import interactionTypes from "../../constants/interactionTypes";
import { useAuth } from "../../store/AuthContext";
import { getAllUsers } from "../../services/userApis";
import { getAllContacts } from "../../services/contactApis";
import {
  createInteraction,
  deleteInteraction,
} from "../../services/interactionApis";

interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
  active: boolean;
}

interface Contact {
  id: number;
  firstName: string;
  lastName: string;
  role: string;
  email: string;
  phone: string;
  isPrimary: boolean;
  createdAt: string;
}

interface Interaction {
  id: number;
  interactionType: string;
  interactionDate: string;
  notes: string;
  followUpDate: string;
  createdAt: string;
  user: User;
  contact: Contact;
}

interface Props {
  restaurantId: number;
  interactions: Interaction[];
}

const Interactions: React.FC<Props> = ({
  restaurantId,
  interactions: initialInteractions,
}) => {
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [users, setUsers] = useState<User[]>([]);
  const [contacts, setContacts] = useState<Contact[]>([]);
  const [interactions, setInteractions] =
    useState<Interaction[]>(initialInteractions);
  const { token } = useAuth();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [usersData, contactsData] = await Promise.all([
          getAllUsers(token || ""),
          getAllContacts(token || "", restaurantId),
        ]);
        setUsers(usersData);
        setContacts(contactsData);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchData();
  }, [token, restaurantId]);

  // Function to handle adding new interaction
  const handleAdd = async (values: any) => {
    try {
      const newInteraction = {
        restaurantId,
        interactionType: values.interactionType,
        interactionDate: values.interactionDate.toISOString(),
        notes: values.notes,
        followUpDate: values.followUpDate.toISOString(),
        userId: values.userId,
        contactId: values.contactId,
      };

      setLoading(true);
      const createdInteraction = await createInteraction(
        newInteraction,
        token || "",
        restaurantId,
        values.userId,
        values.contactId
      );
      setLoading(false);
      message.success("Interaction added successfully");

      // Update local state
      setInteractions((prevInteractions) => [
        ...prevInteractions,
        {
          ...createdInteraction,
          user: users.find((u) => u.id === values.userId)!,
          contact: contacts.find((c) => c.id === values.contactId)!,
        },
      ]);

      form.resetFields();
      setIsModalVisible(false);
    } catch (error) {
      setLoading(false);
      message.error(
        JSON.stringify(error?.response?.data?.details) ||
          JSON.stringify(error?.response?.data?.message) ||
          "Failed to add interaction"
      );
      console.error("Error adding interaction:", error);
    }
  };

  // Function to handle deleting interaction
  const handleDelete = async (id: number) => {
    try {
      await deleteInteraction(id, token || "", restaurantId);
      message.success("Interaction deleted successfully");

      // Update local state
      setInteractions((prevInteractions) =>
        prevInteractions.filter((interaction) => interaction.id !== id)
      );
    } catch (error) {
      message.error(
        JSON.stringify(error?.response?.data?.details) ||
          JSON.stringify(error?.response?.data?.message) ||
          "Failed to delete interaction"
      );
      console.error("Error deleting interaction:", error);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <Typography.Title level={4}>Interactions History</Typography.Title>
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => setIsModalVisible(true)}
          className={styles.addButton}
        >
          Add New Interaction
        </Button>
      </div>

      <div className={styles.interactionsGrid}>
        {interactions.map((interaction) => (
          <Card
            key={interaction.id}
            className={styles.interactionCard}
            title={
              <div className={styles.cardHeader}>
                <span className={styles.type}>
                  {interaction.interactionType}
                </span>
                <Button
                  danger
                  icon={<DeleteOutlined />}
                  onClick={() => handleDelete(interaction.id)}
                />
              </div>
            }
          >
            <div className={styles.cardContent}>
              <div className={styles.mainInfo}>
                <div className={styles.dateInfo}>
                  <p>
                    <strong>Interaction Date:</strong>{" "}
                    {dayjs(interaction.interactionDate).format(
                      "MMM DD, YYYY HH:mm"
                    )}
                  </p>
                  <p>
                    <strong>Follow-up Date:</strong>{" "}
                    {dayjs(interaction.followUpDate).format(
                      "MMM DD, YYYY HH:mm"
                    )}
                  </p>
                </div>
                <div className={styles.notes}>
                  <strong>Notes:</strong>
                  <p>{interaction.notes}</p>
                </div>
              </div>

              <div className={styles.peopleInfo}>
                <div className={styles.contactSection}>
                  <h4>Contact Information</h4>
                  <div className={styles.infoGrid}>
                    <p>
                      <strong>Name:</strong> {interaction.contact.firstName}{" "}
                      {interaction.contact.lastName}
                    </p>
                    <p>
                      <strong>Role:</strong> {interaction.contact.role}
                    </p>
                    <p>
                      <strong>Email:</strong> {interaction.contact.email}
                    </p>
                    <p>
                      <strong>Phone:</strong> {interaction.contact.phone}
                    </p>
                  </div>
                </div>

                <div className={styles.userSection}>
                  <h4>Handled By</h4>
                  <div className={styles.infoGrid}>
                    <p>
                      <strong>Name:</strong> {interaction.user.firstName}{" "}
                      {interaction.user.lastName}
                    </p>
                    <p>
                      <strong>Role:</strong> {interaction.user.role}
                    </p>
                    <p>
                      <strong>Email:</strong> {interaction.user.email}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </Card>
        ))}
      </div>

      <Modal
        title="Add New Interaction"
        open={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        footer={null}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleAdd}
          className={styles.form}
        >
          <Form.Item
            name="interactionType"
            label="Interaction Type"
            rules={[{ required: true }]}
          >
            <Select>
              {interactionTypes.map((type) => (
                <Select.Option key={type.id} value={type.name}>
                  {type.name}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Row gutter={24} justify="space-between">
            <Col xs={24} sm={12}>
              <Form.Item
                name="contactId"
                label="Contact"
                rules={[{ required: true }]}
              >
                <Select>
                  {contacts.map((contact) => (
                    <Select.Option key={contact.id} value={contact.id}>
                      {`${contact.firstName} ${contact.lastName}`}
                    </Select.Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>
            <Col xs={24} sm={12}>
              <Form.Item
                name="userId"
                label="User"
                rules={[{ required: true }]}
              >
                <Select>
                  {users.map((user) => (
                    <Select.Option key={user.id} value={user.id}>
                      {user.username}
                    </Select.Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24} justify="space-between">
            <Col xs={24} sm={12}>
              <Form.Item
                name="interactionDate"
                label="Interaction Date"
                rules={[{ required: true }]}
              >
                <DatePicker showTime className={styles.datePicker} />
              </Form.Item>
            </Col>
            <Col xs={24} sm={12}>
              <Form.Item
                name="followUpDate"
                label="Follow-up Date"
                rules={[{ required: true }]}
              >
                <DatePicker showTime className={styles.datePicker} />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item name="notes" label="Notes" rules={[{ required: true }]}>
            <Input.TextArea rows={4} />
          </Form.Item>

          <Form.Item className={styles.formButtons}>
            <Space>
              <Button type="primary" htmlType="submit">
                Add Interaction
              </Button>
              <Button onClick={() => setIsModalVisible(false)}>Cancel</Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default Interactions;
