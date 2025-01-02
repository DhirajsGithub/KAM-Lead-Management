import React, { useState } from "react";
import {
  Modal,
  Form,
  Input,
  InputNumber,
  Button,
  Select,
  notification,
  Row,
  Col,
  Space,
} from "antd";
import { createRestaurant } from "../../services/restaurantApis";
import { LEAD_STATUS } from "../../constants/leadStatusContstants";
import { useAuth } from "../../store/AuthContext";

const { Option } = Select;

interface AddRestaurantModalProps {
  visible: boolean;
  onCancel: () => void;
  onSuccess: () => void;
}

const AddRestaurantModal: React.FC<AddRestaurantModalProps> = ({
  visible,
  onCancel,
  onSuccess,
}) => {
  const { token } = useAuth(); // Access token from AuthContext
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (values: any) => {
    setLoading(true);
    try {
      const response = await createRestaurant(values, token || "");
      console.log(response);
      if (response) {
        notification.success({
          message: "Restaurant Added Successfully",
          description: "The new restaurant has been added.",
        });
        onSuccess();
        onCancel(); // Close modal after success
      } else {
        notification.error({
          message: "Error",
          description: "Failed to add restaurant. Please try again.",
        });
      }
    } catch (error) {
      notification.error({
        message: "Error",
        description:
          JSON.stringify(error?.response?.data?.details) ||
          "Failed to add restaurant. Please try again.",
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal
      title="Add New Restaurant"
      open={visible}
      onCancel={onCancel}
      footer={null}
      destroyOnClose
      width={700} // Set a fixed width for consistency
      centered
    >
      <Form
        layout="vertical"
        onFinish={handleSubmit}
        initialValues={{
          leadStatus: LEAD_STATUS.NEW,
        }}
      >
        <Row gutter={24}>
          {/* Left Column */}
          <Col xs={24} sm={12}>
            <Form.Item
              label="Restaurant Name"
              name="name"
              rules={[
                {
                  required: true,
                  message: "Please input the restaurant name!",
                },
              ]}
            >
              <Input placeholder="Enter restaurant name" />
            </Form.Item>

            <Form.Item
              label="Address"
              name="address"
              rules={[{ required: true, message: "Please input the address!" }]}
            >
              <Input placeholder="Enter address" />
            </Form.Item>

            <Form.Item
              label="City"
              name="city"
              rules={[{ required: true, message: "Please input the city!" }]}
            >
              <Input placeholder="Enter city" />
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
          </Col>

          {/* Right Column */}
          <Col xs={24} sm={12}>
            <Form.Item
              label="State"
              name="state"
              rules={[{ required: true, message: "Please input the state!" }]}
            >
              <Input placeholder="Enter state" />
            </Form.Item>

            <Form.Item
              label="Annual Revenue"
              name="annualRevenue"
              rules={[
                { required: true, message: "Please input the annual revenue!" },
              ]}
            >
              <InputNumber
                style={{ width: "100%" }}
                min={0}
                step={0.01}
                type="number"
                placeholder="Enter revenue"
              />
            </Form.Item>

            <Form.Item
              label="Lead Status"
              name="leadStatus"
              rules={[
                { required: true, message: "Please select the lead status!" },
              ]}
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
                {
                  required: true,
                  type: "email",
                  message: "Please input a valid email!",
                },
              ]}
            >
              <Input placeholder="Enter email" />
            </Form.Item>
          </Col>
        </Row>
        <Row>
          <Button
            type="primary"
            htmlType="submit"
            loading={loading}
            block
            size="large" // Larger button for better touch target
          >
            Add Restaurant
          </Button>
        </Row>
      </Form>
    </Modal>
  );
};

export default AddRestaurantModal;
