import React, { createContext, useState, useEffect, useContext } from 'react';
import { getCurrentUser } from '../services/userApis';

type User = {
  id: string;
  name: string;
  email: string;
};

type AuthContextType = {
  token: string | null;
  user: User | null;
  login: (token: string) => void;
  logout: () => void;
  fetchUserDetails: (token: string) => Promise<void>;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [token, setToken] = useState<string | null>(null);
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    // Retrieve the token from local storage on app start
    const storedToken = localStorage.getItem('authToken');
    if (storedToken) {
      setToken(storedToken);
      fetchUserDetails(storedToken);
    }
  }, []);

  const login = (newToken: string) => {
    setToken(newToken);
    localStorage.setItem('authToken', newToken);
    fetchUserDetails(newToken);
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('authToken');
  };

  const fetchUserDetails = async (currentToken: string | null = token) => {
    if (!currentToken) return;
  
    try {
      const userData = await getCurrentUser(currentToken);
      setUser(userData); // Assuming `setUser` is part of your state management.
    } catch (error) {
      console.error('Error fetching user details:', error);
      setUser(null); // Clear user state if fetching fails.
    }
  };

  return (
    <AuthContext.Provider value={{ token, user, login, logout, fetchUserDetails }}>
      {children}
    </AuthContext.Provider>
  );
};

const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export { AuthProvider, useAuth };
