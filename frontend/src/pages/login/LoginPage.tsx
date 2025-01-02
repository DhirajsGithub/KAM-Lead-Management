import React, { useEffect, useState } from "react";
import {
  Form,
  Input,
  Button,
  Typography,
  Card,
  Spin,
  Divider,
  Tag,
} from "antd";
import styles from "./login.module.css";
import { useAuth } from "../../store/AuthContext";
import { useNavigate } from "react-router-dom";
import { login as loginApi, validateToken } from "../../services/userApis";

const { Title } = Typography;

const LoginPage: React.FC = () => {
  const { login, token } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [form] = Form.useForm();

    const isTokenValid = async () => {
      try {
        const response = await validateToken(token|| "");
        if(response?.valid){
          navigate("/protected/restaurants");
        }
        return response.valid;
      } catch (error) { 
        console.error('Token validation failed:', error);
        // navigate('/');
        return false;
      }
      finally{
        setLoading(false);
      }
    }

  useEffect(() => {
    isTokenValid()
  }, [token, navigate]);

  const onFill = (username: string, password: string) => {
    form.setFieldsValue({ username, password });
  };

  const onFinish = async (values: { username: string; password: string }) => {
    setLoading(true);
    setError(null);

    try {
      const response = await loginApi(values.username, values.password);
      login(response.token);
      navigate("/protected/restaurants");
    } catch (error) {
      setError("Login failed. Please check your username and password.");
    } finally {
      setLoading(false);
    }
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  return (
    <div className={styles.container}>
      <Card className={styles.card}>
        <Title level={3} className={styles.title}>
          Login
        </Title>
        <div className={styles.credentialsContainer}>
          <Divider>Quick Access</Divider>
          <div className={styles.credentialCards}>
            <div
              className={styles.credentialCard}
              onClick={() => onFill("admin", "admin123")}
            >
       
              <div className={styles.roleTag}>
                <Tag color="gold">Admin</Tag>
              </div>
              <div className={styles.userInfo}>
                <div>admin</div>
                <div className={styles.dot}>•</div>
                <div>admin123</div>
              </div>
            </div>

            <div
              className={styles.credentialCard}
              onClick={() => onFill("dhiraj", "dhiraj")}
            >
              <div className={styles.roleTag}>
                <Tag color="cyan">Manager</Tag>
              </div>
              <div className={styles.userInfo}>
                <div>dhiraj</div>
                <div className={styles.dot}>•</div>
                <div>dhiraj</div>
              </div>
            </div>
          </div>
        </div>
        {error && <p className={styles.error}>{error}</p>}
        <Form
          form={form}
          name="login"
          initialValues={{ remember: true }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          layout="vertical"
          className={styles.form}
        >
          <Form.Item
            label="Username"
            name="username"
            rules={[{ required: true, message: "Please input your username!" }]}
          >
            <Input placeholder="Enter your username" />
          </Form.Item>

          <Form.Item
            label="Password"
            name="password"
            rules={[{ required: true, message: "Please input your password!" }]}
          >
            <Input.Password placeholder="Enter your password" />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" block disabled={loading}>
              {loading ? <Spin /> : "Login"}
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default LoginPage;
