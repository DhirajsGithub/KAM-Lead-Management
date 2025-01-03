import axios from 'axios';
import baseUrl from '../constants/baseUrl';

const API_BASE_URL = baseUrl + "/performance_metrics";

// Get Performance Metrics for a Restaurant API
export const getMetricsByRestaurantId = async (token: string, restaurantId: number) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${restaurantId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting list of performance metrics
  } catch (error) {
    console.error('Fetching performance metrics by restaurant ID failed:', error);
    throw error;
  }
};

// Create Performance Metric API
export const createMetric = async (
  metric: any,
  token: string,
  restaurantId: number
) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/${restaurantId}`, metric, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting created performance metric details
  } catch (error) {
    console.error('Creating performance metric failed:', error);
    throw error;
  }
};

// Update Performance Metric API
export const updateMetric = async (
  metricId: number,
  updatedMetric: any,
  token: string,
  restaurantId: number
) => {
  try {
    const response = await axios.put(
      `${API_BASE_URL}/${restaurantId}/${metricId}`,
      updatedMetric,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data; // Expecting updated performance metric details
  } catch (error) {
    console.error('Updating performance metric failed:', error);
    throw error;
  }
};

// Delete Performance Metric API
export const deleteMetric = async (
  metricId: number,
  token: string,
  restaurantId: number
) => {
  try {
    await axios.delete(`${API_BASE_URL}/${restaurantId}/${metricId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (error) {
    console.error('Deleting performance metric failed:', error);
    throw error;
  }
};
