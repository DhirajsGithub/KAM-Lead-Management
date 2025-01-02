import React from 'react';
import { Button, Result } from 'antd';
import { useNavigate } from 'react-router-dom';
import styles from './extra.module.css';

const PageNotFound: React.FC = () => {
  const navigate = useNavigate();

  const handleBackHome = () => {
    navigate('/protected/restaurants'); // Redirect to the home page
  };

  return (
    <div className={styles.container}>
      <Result
        status="404"
        title="404"
        subTitle="Sorry, the page you visited does not exist."
        extra={
          <Button type="primary" onClick={handleBackHome}>
            Back Home
          </Button>
        }
      />
    </div>
  );
};

export default PageNotFound;
