import axios from 'axios';
import baseUrl from '../constants/baseUrl';

const API_BASE_URL = baseUrl + "/orders";

// Get All Orders for a Restaurant API
export const getOrdersByRestaurantId = async (token: string, restaurantId: number) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${restaurantId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting list of orders
  } catch (error) {
    console.error('Fetching orders by restaurant ID failed:', error);
    throw error;
  }
};

// Create Order API
export const createOrder = async (
  order: any,
  token: string,
  restaurantId: number
) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/${restaurantId}`, order, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting created order details
  } catch (error) {
    console.error('Creating order failed:', error);
    throw error;
  }
};

// Update Order API
export const updateOrder = async (
  orderId: number,
  updatedOrder: any,
  token: string,
  restaurantId: number
) => {
  try {
    const response = await axios.put(
      `${API_BASE_URL}/${restaurantId}/${orderId}`,
      updatedOrder,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data; // Expecting updated order details
  } catch (error) {
    console.error('Updating order failed:', error);
    throw error;
  }
};

// Delete Order API
export const deleteOrder = async (
  orderId: number,
  token: string,
  restaurantId: number
) => {
  try {
    await axios.delete(`${API_BASE_URL}/${restaurantId}/${orderId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (error) {
    console.error('Deleting order failed:', error);
    throw error;
  }
};
