import React, { useState, useEffect } from "react";
import {
  Table,
  Button,
  Modal,
  Form,
  Input,
  Select,
  Space,
  message,
  Row,
  Col,
  Tag,
} from "antd";
import {
  EditOutlined,
  DeleteOutlined,
  UserAddOutlined,
} from "@ant-design/icons";
import styles from "./styles/Users.module.css";
import { deleteUser, register, updateUser } from "../../services/userApis";
import { useAuth } from "../../store/AuthContext";
import { addUserToRestaurant } from "../../services/restaurantUserApis";

const { Option } = Select;

interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
  active: boolean;
}

interface UsersProps {
  users: User[];
  restaurantId: number;
}

const ROLES = {
  MANAGER: "MANAGER",
  ADMIN: "ADMIN",
};

const Users: React.FC<UsersProps> = ({ users: initialUsers, restaurantId }) => {
  const [users, setUsers] = useState<User[]>(initialUsers); // Local state for users
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingUser, setEditingUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const { token, user } = useAuth();

  useEffect(() => {
    setUsers(initialUsers); // Sync with the initial prop if it changes
  }, [initialUsers]);

  const handleAdd = () => {
    setEditingUser(null);
    form.resetFields();
    setIsModalVisible(true);
  };

  const handleEdit = (user: User) => {
    setEditingUser(user);
    form.setFieldsValue(user);
    setIsModalVisible(true);
  };

  const handleDelete = async (userId: number) => {
    try {
      await deleteUser(userId, token || "");
      setUsers(users.filter((user) => user.id !== userId)); // Update the users state
      message.success("User deleted successfully");
    } catch (error) {
      message.error(
        JSON.stringify(error?.response?.data?.details) ||
          JSON.stringify(error?.response?.data?.message) ||
          "Failed to delete user"
      );
    }
  };

  const handleSubmit = async (values: any) => {
    setLoading(true);
    try {
      if (editingUser) {
        await updateUser(editingUser.id, values, token || "");
        setUsers(
          users.map((user) =>
            user.id === editingUser.id ? { ...user, ...values } : user
          )
        ); // Update the user in the list
        message.success("User updated successfully");
      } else {
        let res = await register(values, token || "");
        if (res.id) {
          await addUserToRestaurant(
            {
              restaurant: { id: restaurantId },
              user: { id: res.id },
              isActive: true,
            },
            token || ""
          );
          setUsers([...users, res]); // Add the new user to the list
        }
        message.success("User assigned successfully");
      }
      setIsModalVisible(false);
    } catch (error) {
      message.error(
        JSON.stringify(error?.response?.data?.details) ||
          JSON.stringify(error?.response?.data?.message) ||
          "Failed to save user"
      );
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    {
      title: "Name",
      key: "name",
      render: (text: string, record: User) =>
        `${record.firstName} ${record.lastName}`,
    },
    {
      title: "Username",
      dataIndex: "username",
      key: "username",
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
    },
    {
      title: "Role",
      dataIndex: "role",
      render: (role: string) => (
        <Tag
          color={role === "ADMIN" ? "gold" : "cyan"}
          className={styles.roleTag}
        >
          {role || "User"}
        </Tag>
      ),
      key: "role",
    },
    {
      title: "Status",
      dataIndex: "active",
      key: "active",
      render: (active: boolean) => (
        <span className={active ? styles.activeStatus : styles.inactiveStatus}>
          {active ? "Active" : "Inactive"}
        </span>
      ),
    },
    ...(user?.role === "ADMIN"
      ? [
          {
            title: "Actions",

            key: "actions",
            render: (text: string, record: User) => (
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
        ]
      : []),
  ];

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        {user?.role == "ADMIN" && (
          <Button
            type="primary"
            icon={<UserAddOutlined />}
            onClick={handleAdd}
            className={styles.addButton}
          >
            Assign User
          </Button>
        )}
      </div>

      <Table
        columns={columns}
        dataSource={users}
        rowKey="id"
        pagination={{ pageSize: 10 }}
        style={{ minWidth: "800px" }}
      />

      <Modal
        title={
          editingUser
            ? "Edit User"
            : "Create New User/Manager and assign to a restaurant"
        }
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
                label="Username"
                name="username"
                rules={[
                  { required: true, message: "Please input the username!" },
                ]}
              >
                <Input placeholder="Enter username" />
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
              {!editingUser && (
                <Form.Item
                  label="Password"
                  name="password"
                  rules={[
                    {
                      required: true,
                      message: "Please input a valid password!",
                    },
                  ]}
                >
                  <Input.Password placeholder="Enter your password" />
                </Form.Item>
              )}
              <Form.Item
                label="Role"
                name="role"
                rules={[{ required: true, message: "Please select a role!" }]}
              >
                <Select placeholder="Select role">
                  {Object.values(ROLES).map((role) => (
                    <Option key={role} value={role}>
                      {role}
                    </Option>
                  ))}
                </Select>
              </Form.Item>

              <Form.Item
                label="Status"
                name="active"
                rules={[{ required: true, message: "Please select status!" }]}
              >
                <Select placeholder="Select status">
                  <Option value={true}>Active</Option>
                  <Option value={false}>Inactive</Option>
                </Select>
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
                {editingUser ? "Save Changes" : "Assign Manager/User"}
              </Button>
            </Col>
          </Row>
        </Form>
      </Modal>
    </div>
  );
};

export default Users;
