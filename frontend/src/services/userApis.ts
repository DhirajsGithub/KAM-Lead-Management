import axios from 'axios';
import baseUrl from '../constants/baseUrl';

const API_BASE_URL = baseUrl;

// Login API
export const login = async (username: string, password: string) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/auth/login`, {
      username,
      password,
    });
    return response.data; // Expecting { token: string }
  } catch (error) {
    console.error('Login failed:', error);
    throw error;
  }
};

// Register API
export const register = async (user: { username: string; password: string; email: string }) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/auth/register`, user);
    return response.data; // Expecting user details
  } catch (error) {
    console.error('Registration failed:', error);
    throw error;
  }
};

// Get Current User Details API
export const getCurrentUser = async (token: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/users/current`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting user details
  } catch (error) {
    console.error('Fetching current user failed:', error);
    throw error;
  }
};

// Get All Users API
export const getAllUsers = async (token: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/users`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting list of users
  } catch (error) {
    console.error('Fetching all users failed:', error);
    throw error;
  }
};

// Update User API
export const updateUser = async (id: number, updatedUser: { name?: string; password?: string }, token: string) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/users/${id}`, updatedUser, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting updated user
  } catch (error) {
    console.error('Updating user failed:', error);
    throw error;
  }
};

// Delete User API
export const deleteUser = async (id: number, token: string) => {
  try {
    await axios.delete(`${API_BASE_URL}/users/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (error) {
    console.error('Deleting user failed:', error);
    throw error;
  }
};

// Validate Token API
export const validateToken = async (token: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/auth/validate-token`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting { valid: boolean }
  } catch (error) {
    console.error('Token validation failed:', error);
    throw error;
  }
};
