import React, { useEffect, useState } from 'react';
import { Navigate, Outlet, useNavigate } from 'react-router-dom';
import { message, Spin } from 'antd';
import { useAuth } from '../../store/AuthContext';
import { validateToken } from '../../services/userApis'; 
import AppHeader from '../../components/layout/Header';

const ProtectedPages: React.FC = () => {
  const { token } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState<boolean>(true); 

  const isTokenValid = async () => {
    try {
      if(!token){
        message.error('Please login to access this page');
        navigate('/');
        return
      };
      const response = await validateToken(token || "");
      if(!response.valid){
        navigate('/');
        message.error('Token validation failed');
        return;
      }
      return response.valid;
    } catch (error) { 
      console.error('Token validation failed:', error);
      
      navigate('/');
      return false;
    }
    finally{
      setLoading(false);
    }
  }

  useEffect(() => {
    isTokenValid()
  }, [token, navigate]);

  if (loading || !token) {
    return <Spin size="large" style={{ display: 'block', margin: '20% auto' }} />;
  }
  

  return token && !loading ? (
    <div>
      <AppHeader />
      <Outlet />
    </div>
  ) : (
    <Navigate to="/" />
  );
};

export default ProtectedPages;
