import axios from 'axios';
import baseUrl from '../constants/baseUrl';

const API_BASE_URL = baseUrl + '/restaurant';

// Helper to attach token for authorization
const getAuthHeaders = (token: string) => ({
  headers: {
    Authorization: `Bearer ${token}`,
  },
});

// Create a Restaurant
export const createRestaurant = async (restaurant: any, token: string) => {
  console.log(restaurant)
  console.log(token)
  try {
    const response = await axios.post(API_BASE_URL, restaurant, getAuthHeaders(token));
    return response.data; // Expecting RestaurantDTO
  } catch (error) {
    console.error('Creating restaurant failed:', error);
    throw error;
  }
};

// Get Restaurant by ID
export const getRestaurantById = async (restaurantId: number, token: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${restaurantId}`, getAuthHeaders(token));
    return response.data; // Expecting RestaurantDetailDTO
  } catch (error) {
    console.error('Fetching restaurant by ID failed:', error);
    throw error;
  }
};

// Get All Restaurants (with filters)
export const getRestaurants = async (
  params: {
    leadStatus?: string;
    city?: string;
    search?: string;
    page?: number;
    size?: number;
  },
  token: string
) => {
  try {
    const response = await axios.get(API_BASE_URL, {
      params,
      ...getAuthHeaders(token),
    });
    return response.data; // Expecting Page<RestaurantDTO>
  } catch (error) {
    console.error('Fetching restaurants failed:', error);
    throw error;
  }
};

// Update Restaurant
export const updateRestaurant = async (restaurantId: number, restaurant: any, token: string) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/${restaurantId}`, restaurant, getAuthHeaders(token));
    return response.data; // Expecting updated RestaurantDTO
  } catch (error) {
    console.error('Updating restaurant failed:', error);
    throw error;
  }
};

// Delete Restaurant
export const deleteRestaurant = async (restaurantId: number, token: string) => {
  try {
    await axios.delete(`${API_BASE_URL}/${restaurantId}`, getAuthHeaders(token));
  } catch (error) {
    console.error('Deleting restaurant failed:', error);
    throw error;
  }
};

// Get Contacts by Restaurant ID
export const getContactsByRestaurantId = async (restaurantId: number, token: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${restaurantId}/contacts`, getAuthHeaders(token));
    return response.data; // Expecting list of contacts
  } catch (error) {
    console.error('Fetching contacts failed:', error);
    throw error;
  }
};

// Get Call Schedules by Restaurant ID
export const getCallSchedulesByRestaurantId = async (restaurantId: number, token: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${restaurantId}/callschedules`, getAuthHeaders(token));
    return response.data; // Expecting list of call schedules
  } catch (error) {
    console.error('Fetching call schedules failed:', error);
    throw error;
  }
};

// Get Interactions by Restaurant ID
export const getInteractionsByRestaurantId = async (restaurantId: number, token: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${restaurantId}/interactions`, getAuthHeaders(token));
    return response.data; // Expecting list of interactions
  } catch (error) {
    console.error('Fetching interactions failed:', error);
    throw error;
  }
};

// Get Orders by Restaurant ID
export const getOrdersByRestaurantId = async (restaurantId: number, token: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${restaurantId}/orders`, getAuthHeaders(token));
    return response.data; // Expecting list of orders
  } catch (error) {
    console.error('Fetching orders failed:', error);
    throw error;
  }
};

// Get Performance Metrics by Restaurant ID
export const getPerformanceMetricsByRestaurantId = async (restaurantId: number, token: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${restaurantId}/performance`, getAuthHeaders(token));
    return response.data; // Expecting list of performance metrics
  } catch (error) {
    console.error('Fetching performance metrics failed:', error);
    throw error;
  }
};
