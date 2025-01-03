import React, { useState, useEffect } from "react";
import {
  Table,
  Button,
  Modal,
  Form,
  Input,
  Space,
  Row,
  Col,
  Switch,
  message,
} from "antd";
import {
  EditOutlined,
  DeleteOutlined,
  UserAddOutlined,
} from "@ant-design/icons";
import styles from "./styles/Contacts.module.css";
import { useAuth } from "../../store/AuthContext";
import {
  createContact,
  deleteContact,
  updateContact,
} from "../../services/contactApis";

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

interface ContactsProps {
  contacts: Contact[];
  restaurantId: number;
}

const Contacts: React.FC<ContactsProps> = ({
  contacts: initialContacts,
  restaurantId,
}) => {
  const [contacts, setContacts] = useState<Contact[]>(initialContacts);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingContact, setEditingContact] = useState<Contact | null>(null);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const { token } = useAuth();

  useEffect(() => {
    setContacts(initialContacts); // Sync with the initial prop if it changes
  }, [initialContacts]);

  const handleAdd = () => {
    setEditingContact(null);
    form.resetFields();
    setIsModalVisible(true);
  };

  const handleEdit = (contact: Contact) => {
    setEditingContact(contact);
    form.setFieldsValue(contact);
    setIsModalVisible(true);
  };

  const handleDelete = async (contactId: number) => {
    try {
      await deleteContact(contactId, token || "", restaurantId);
      setContacts(contacts.filter((contact) => contact.id !== contactId)); // Remove contact from state
      message.success("Contact deleted successfully");
    } catch (error) {
      message.error(
        JSON.stringify(error?.response?.data?.details) ||
          JSON.stringify(error?.response?.data?.message) ||
          "Failed to delete contact"
      );
    }
  };

  const handleSubmit = async (values: any) => {
    setLoading(true);
    try {
      if (editingContact) {
        // Update the contact locally
        await updateContact(
          editingContact.id,
          values,
          token || "",
          restaurantId
        );
        setContacts(
          contacts.map((contact) =>
            contact.id === editingContact.id
              ? { ...contact, ...values }
              : contact
          )
        );
        message.success("Contact updated successfully");
      } else {
        // Create a new contact locally
        const newContact = await createContact(
          values,
          token || "",
          restaurantId
        );
        setContacts([...contacts, newContact]);
        message.success("Contact created successfully");
      }
      setIsModalVisible(false);
    } catch (error) {
       message.error(JSON.stringify(error?.response?.data?.details) || JSON.stringify(error?.response?.data?.message) || "Failed to save contact");
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    {
      title: "Name",
      key: "name",
      render: (text: string, record: Contact) =>
        `${record.firstName} ${record.lastName}`,
    },
    {
      title: "Role",
      dataIndex: "role",
      key: "role",
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
    },
    {
      title: "Phone",
      dataIndex: "phone",
      key: "phone",
    },
    {
      title: "Primary Contact",
      dataIndex: "isPrimary",
      key: "isPrimary",
      render: (isPrimary: boolean) => (
        <span className={isPrimary ? styles.primary : styles.secondary}>
          {isPrimary ? "Primary" : "Secondary"}
        </span>
      ),
    },
    {
      title: "Created At",
      dataIndex: "createdAt",
      key: "createdAt",
      render: (date: string) => new Date(date).toLocaleDateString(),
    },
    {
      title: "Actions",
      key: "actions",
      render: (text: string, record: Contact) => (
        <Space size="middle">
          <Button icon={<EditOutlined />} onClick={() => handleEdit(record)} />
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
          icon={<UserAddOutlined />}
          onClick={handleAdd}
          className={styles.addButton}
        >
          Add Contact
        </Button>
      </div>

      <Table
        columns={columns}
        dataSource={contacts}
        rowKey="id"
        pagination={{ pageSize: 10 }}
        style={{ minWidth: "800px" }}
      />

      <Modal
        title={editingContact ? "Edit Contact" : "Add New Contact"}
        open={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        footer={null}
        width={700}
        centered
        destroyOnClose
      >
        <Form form={form} layout="vertical" onFinish={handleSubmit}>
          <Row gutter={24}>
            <Col xs={24} sm={12}>
              <Form.Item
                label="First Name"
                name="firstName"
                rules={[
                  { required: true, message: "Please input the first name!" },
                ]}
              >
                <Input placeholder="Enter first name" />
              </Form.Item>

              <Form.Item
                label="Last Name"
                name="lastName"
                rules={[
                  { required: true, message: "Please input the last name!" },
                ]}
              >
                <Input placeholder="Enter last name" />
              </Form.Item>

              <Form.Item
                label="Role"
                name="role"
                rules={[{ required: true, message: "Please input the role!" }]}
              >
                <Input placeholder="Enter role" />
              </Form.Item>
            </Col>

            <Col xs={24} sm={12}>
              <Form.Item
                label="Email"
                name="email"
                rules={[
                  {
                    required: true,
                    type: "email",
                    message: "Please input a valid email!",
                  },
                ]}
              >
                <Input placeholder="Enter email" />
              </Form.Item>

              <Form.Item
                label="Phone"
                name="phone"
                rules={[
                  { required: true, message: "Please input the phone number!" },
                ]}
              >
                <Input placeholder="Enter phone number" />
              </Form.Item>

              <Form.Item
                label="Primary Contact"
                name="isPrimary"
                valuePropName="checked"
              >
                <Switch />
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
                {editingContact ? "Save Changes" : "Add Contact"}
              </Button>
            </Col>
          </Row>
        </Form>
      </Modal>
    </div>
  );
};

export default Contacts;
