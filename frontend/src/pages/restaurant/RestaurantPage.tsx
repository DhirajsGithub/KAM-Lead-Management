import React, { useEffect, useState } from 'react';
import { Button, Card, Spin, Tabs } from 'antd';
import { useNavigate, useParams } from 'react-router-dom';
import { LeftOutlined } from '@ant-design/icons'; // Ant Design icon

import styles from './RestaurantPage.module.css';
import RestaurantInfo from '../../components/restaurant/RestaurantInfo';
import { getRestaurantById } from '../../services/restaurantApis';
import { useAuth } from '../../store/AuthContext';
import Users from '../../components/restaurant/Users';
import Contacts from '../../components/restaurant/Contacts';
import CallSchedules from '../../components/restaurant/CallSchedules';
import Interactions from '../../components/restaurant/Interactions';
import Orders from '../../components/restaurant/Orders';
import PerformanceMetrics from '../../components/restaurant/PerformanceMetrics';

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
  users: User[];
  contacts: Contact[];
  callSchedules: CallSchedule[];
  interactions: Interaction[];
  orders: Order[];
  performanceMetrices: PerformanceMetric[];
}

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

interface CallSchedule {
  id: number;
  frequencyDays: number;
  lastCallDate: string;
  nextCallDate: string;
  priorityLevel: number;
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

interface Order {
  id: number;
  orderDate: string;
  totalAmount: number;
  status: string;
  createdAt: string;
}

interface PerformanceMetric {
  id: number;
  metricDate: string;
  leadsCount: number;
  closedDeals: number;
  revenue: number;
  followUpsCount: number;
  createdAt: string;
}

const RestaurantPage: React.FC = () => {
  const navigate = useNavigate(); // For navigation


  const { id } = useParams();
  const [restaurant, setRestaurant] = useState<Restaurant | null>(null);
  const {token} = useAuth();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchRestaurant = async () => {
      try {
        const data = await getRestaurantById(Number(id), token || '');
        setRestaurant(data);
      } catch (error) {
        console.error('Error fetching restaurant:', error);
        setLoading(false);
      } finally {
        setLoading(false);
      }
    };

    fetchRestaurant();
  }, [id]);

  if (loading) {
    return <Spin className={styles.spinner} size="large" />;
  }

  if (!restaurant) {
    return <div>Restaurant not found</div>;
  }

  const items = [
    {
      key: '1',
      label: 'Info',
      children: <RestaurantInfo restaurant={restaurant} />,
    },
    {
      key: '2',
      label: 'Managers',
      children: <Users restaurantId={restaurant.id} users={restaurant.users} />,
    },
    {
      key: '3',
      label: 'Contacts',
      children: <Contacts restaurantId={restaurant.id} contacts={restaurant.contacts} />,
    },
    {
      key: '4',
      label: 'Call Schedules',
      children: <CallSchedules restaurantId={restaurant.id} schedules={restaurant.callSchedules} />,
    },
    {
      key: '5',
      label: 'Interactions',
      children: <Interactions restaurantId={restaurant.id} interactions={restaurant.interactions} />,
    },
    {
      key: '6',
      label: 'Orders',
      children: <Orders restaurantId={restaurant.id} orders={restaurant.orders} />,
    },
    {
      key: '7',
      label: 'Performance',
      children: <PerformanceMetrics restaurantId={restaurant.id} metrics={restaurant.performanceMetrices} />,
    },
  ];

  const handleBack = () => {
    navigate("/protected/restaurants"); 
  };

  return (
    <div className={styles.container}>
      <Card
        title={
          <div className={styles.cardHeader}>
            <Button
              onClick={handleBack}
              className={styles.backButton}
              icon={<LeftOutlined />} // Add the Ant Design back icon
              type="text" // Text button to keep it minimal
            >
              Back
            </Button>
            {restaurant.name}
          </div>
        }
        className={styles.card}
      >
        <Tabs items={items} type="card" className={styles.tabs} />
      </Card>
    </div>
  );
};

export default RestaurantPage;