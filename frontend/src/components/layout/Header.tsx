import { Layout, Avatar, Dropdown, Tag, Menu } from 'antd';
import { UserOutlined, LogoutOutlined, ProfileOutlined } from '@ant-design/icons';
import { useAuth } from '../../store/AuthContext';
import styles from './header.module.css';

const { Header } = Layout;

const AppHeader = () => {
  const { user, logout } = useAuth();

  const items = [
    {
      key: 'username',
      label: <span>{user?.username}</span>,
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: 'Logout',
      onClick: logout,
    },
  ];

  return (
    <Header className={styles.header}>
      <div className={styles.logo}>Restaurant KAM</div>
      <div className={styles.userSection}>
        <span className={styles.username}>
         
          <Tag color={user?.role === "ADMIN" ? "gold" : "cyan"} className={styles.roleTag}>
            {user?.role || 'User'}
          </Tag>
        </span>
        <Dropdown
          overlay={<Menu items={items} />}
          placement="bottomRight"
        >
          <Avatar icon={<UserOutlined />} className={styles.avatar} />
        </Dropdown>
      </div>
    </Header>
  );
};

export default AppHeader;
