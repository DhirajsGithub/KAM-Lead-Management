import React, { useState, useEffect } from "react";
import {
  Table,
  Input,
  Select,
  Card,
  Spin,
  Empty,
  Typography,
  Button,
  Row,
} from "antd";
import type { TableProps } from "antd";
import { PlusOutlined, SearchOutlined } from "@ant-design/icons";
import { useAuth } from "../../store/AuthContext";
import {
  LEAD_STATUS,
  LEAD_STATUS_COLORS,
  LEAD_STATUS_TEXT_COLORS,
} from "../../constants/leadStatusContstants";
import styles from "./restaurants.module.css";
import { getRestaurants } from "../../services/restaurantApis";
import { useNavigate } from "react-router-dom";
import AddRestaurantModal from "../../components/modals/AddRestaurantModal";

const { Title } = Typography;
const { Search } = Input;
const { Option } = Select;

interface Restaurant {
  id: number;
  name: string;
  address: string;
  city: string;
  state: string;
  phone: string;
  email: string;
  createdAt: string;
  leadStatus: string;
  annualRevenue: number;
  timezone: string;
}

interface TableParams {
  leadStatus?: string;
  city?: string;
  search?: string;
  page?: number;
  size?: number;
}

const RestaurantsPage = () => {
  const navigate = useNavigate();
  const { token, user } = useAuth();
  const [displayData, setDisplayData] = useState<Restaurant[]>([]);
  const [loading, setLoading] = useState(false);
  const [params, setParams] = useState<TableParams>({
    page: 1,
    size: 10,
  });
  const [total, setTotal] = useState(0); // Track total number of restaurants
  const [isModalVisible, setIsModalVisible] = useState(false);

  const fetchData = async () => {
    setLoading(true);
    try {
      const cleanParams = {
        ...params,
        page: (params.page || 1) - 1,
        city: params.city || undefined,
        search: params.search || undefined,
        leadStatus: params.leadStatus || undefined,
      };

      const response = await getRestaurants(
        Object.fromEntries(
          Object.entries(cleanParams).filter(
            ([_, value]) => value !== undefined
          )
        ),
        token || ""
      );

      setDisplayData(response.content);
      setTotal(response.totalElements); // Set total number of records for pagination
    } catch (error) {
      console.error("Failed to fetch restaurants:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [params]);

  const handleSearch = (value: string) => {
    setParams((prev) => ({ ...prev, search: value, page: 1 }));
  };

  const handleCitySearch = (value: string) => {
    setParams((prev) => ({ ...prev, city: value, page: 1 }));
  };

  const handleStatusFilter = (value: string) => {
    setParams((prev) => ({ ...prev, leadStatus: value, page: 1 }));
  };

  const handleSort = (column: string, order: "ascend" | "descend" | null) => {
    const sortedData = [...displayData].sort((a: any, b: any) => {
      if (!order) return 0;

      const compareResult =
        column === "annualRevenue"
          ? a[column] - b[column]
          : column === "createdAt"
          ? new Date(a[column]).getTime() - new Date(b[column]).getTime()
          : String(a[column]).localeCompare(String(b[column]));

      return order === "ascend" ? compareResult : -compareResult;
    });

    setDisplayData(sortedData);
  };

  const columns = [
    {
      title: "Name",
      dataIndex: "name",
      sorter: (a: Restaurant, b: Restaurant) => a.name.localeCompare(b.name),
      width: 200,
    },
    {
      title: "City",
      dataIndex: "city",
      sorter: (a: Restaurant, b: Restaurant) => a.city.localeCompare(b.city),
      width: 150,
    },
    {
      title: "State",
      dataIndex: "state",
      sorter: (a: Restaurant, b: Restaurant) => a.state.localeCompare(b.state),
      width: 100,
    },
    {
      title: "Status",
      dataIndex: "leadStatus",
      sorter: (a: Restaurant, b: Restaurant) =>
        a.leadStatus.localeCompare(b.leadStatus),
      width: 120,
      render: (status: keyof typeof LEAD_STATUS) => (
        <span
          className={styles.statusBadge}
          style={{
            backgroundColor: LEAD_STATUS_COLORS[status],
            color: LEAD_STATUS_TEXT_COLORS[status],
          }}
        >
          {status}
        </span>
      ),
    },
    {
      title: "Revenue",
      dataIndex: "annualRevenue",
      sorter: (a: Restaurant, b: Restaurant) =>
        a.annualRevenue - b.annualRevenue,
      width: 120,
      render: (value: number) => `$${value.toLocaleString()}`,
    },
    {
      title: "Contact",
      dataIndex: "phone",
      width: 200,
      render: (phone: string, record: Restaurant) => (
        <div>
          <div>{phone}</div>
          <div className={styles.email}>{record.email}</div>
        </div>
      ),
    },
    {
      title: "Created",
      dataIndex: "createdAt",
      sorter: (a: Restaurant, b: Restaurant) =>
        new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime(),
      width: 150,
      render: (date: string) => new Date(date).toLocaleDateString(),
    },
  ];

  const handleTableChange: TableProps<Restaurant>["onChange"] = (
    pagination,
    filters,
    sorter: any
  ) => {
    if (sorter.column) {
      handleSort(sorter.columnKey, sorter.order);
      return;
    }
  };

  const handlePaginationChange = (page: number, pageSize: number) => {
    setParams((prev) => ({ ...prev, page, size: pageSize }));
  };

  const handleRowClick = (id: number) => {
    navigate(`/protected/restaurants/${id}`);
  };

  return (
    <div className={styles.container}>
      <Card>
        <Row justify="space-between">
          <Title level={3}>Restaurants</Title>

          {user?.role === "ADMIN" && (
            <Button
              type="primary"
              onClick={() => setIsModalVisible(true)}
              style={{ marginBottom: 20 }}
              icon={<PlusOutlined />}
            >
              Add Restaurant
            </Button>
          )}
        </Row>

        <AddRestaurantModal
          visible={isModalVisible}
          onCancel={() => setIsModalVisible(false)}
          onSuccess={fetchData}
        />

        <div className={styles.filters}>
          <Search
            placeholder="Search restaurants..."
            allowClear
            onSearch={handleSearch}
            className={styles.searchInput}
            prefix={<SearchOutlined />}
          />

          <Search
            placeholder="Search by city..."
            allowClear
            onSearch={handleCitySearch}
            className={styles.searchInput}
            prefix={<SearchOutlined />}
          />

          <Select
            placeholder="Filter by status"
            allowClear
            onChange={handleStatusFilter}
            className={styles.filterSelect}
          >
            <Option value="">ALL</Option>
            {Object.values(LEAD_STATUS).map((status) => (
              <Option key={status} value={status}>
                {status}
              </Option>
            ))}
          </Select>
        </div>

        {loading ? (
          <div className={styles.loadingContainer}>
            <Spin size="large" />
          </div>
        ) : displayData.length ? (
          <Table
            columns={columns}
            dataSource={displayData}
            onRow={(record) => ({
              onClick: () => handleRowClick(record.id),
              style: { cursor: "pointer" },
            })}
            rowKey="id"
            pagination={{
              total: total, // Total number of items
              pageSize: params.size,
              current: params.page,
              showSizeChanger: true,
              showTotal: (total) => `Total ${total} restaurants`,
              onChange: handlePaginationChange,
            }}
            onChange={handleTableChange}
            scroll={{ x: "max-content" }}
          />
        ) : (
          <Empty
            description="No restaurants found"
            className={styles.emptyState}
          />
        )}
      </Card>
    </div>
  );
};

export default RestaurantsPage;
