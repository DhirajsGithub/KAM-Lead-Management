import axios from 'axios';
import baseUrl from '../constants/baseUrl';

const API_BASE_URL = `${baseUrl}/restaurant-users`;

// Add a user to a restaurant
export const addUserToRestaurant = async (restaurantUser, token) => {
  try {
    const response = await axios.post(API_BASE_URL, restaurantUser, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting a success message
  } catch (error) {
    console.error('Adding user to restaurant failed:', error);
    throw error;
  }
};

// Delete a user-restaurant relationship
export const deleteUserRestaurant = async (restaurantId, userId, token) => {
  try {
    const response = await axios.delete(API_BASE_URL, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      params: { restaurantId, userId },
    });
    return response.status; // Expecting HTTP 204 No Content on success
  } catch (error) {
    console.error('Deleting user-restaurant relationship failed:', error);
    throw error;
  }
};
