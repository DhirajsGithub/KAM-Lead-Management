import axios from 'axios';
import baseUrl from '../constants/baseUrl';

const API_BASE_URL = baseUrl + "/interactions";

// Get All Interactions for a Restaurant API
export const getInteractionsByRestaurantId = async (token: string, restaurantId: number) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${restaurantId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting list of interactions
  } catch (error) {
    console.error('Fetching interactions by restaurant ID failed:', error);
    throw error;
  }
};

// Create Interaction API
export const createInteraction = async (
  interaction: any,
  token: string,
  restaurantId: number, // Path variable
  requestingUserId: number, // Query parameter
  contactId: number // Query parameter
) => {
  try {
    const response = await axios.post(
      `${API_BASE_URL}/${restaurantId}`,
      interaction,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        params: {
          requestingUserId,
          contactId,
        },
      }
    );
    return response.data; // Expecting created interaction details
  } catch (error) {
    console.error('Creating interaction failed:', error);
    throw error;
  }
};

// Update Interaction API
export const updateInteraction = async (
  interactionId: number,
  updatedInteraction: any,
  token: string,
  restaurantId: number
) => {
  try {
    const response = await axios.put(
      `${API_BASE_URL}/${restaurantId}/${interactionId}`,
      updatedInteraction,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data; // Expecting updated interaction details
  } catch (error) {
    console.error('Updating interaction failed:', error);
    throw error;
  }
};

// Delete Interaction API
export const deleteInteraction = async (
  interactionId: number,
  token: string,
  restaurantId: number
) => {
  try {
    await axios.delete(`${API_BASE_URL}/${restaurantId}/${interactionId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (error) {
    console.error('Deleting interaction failed:', error);
    throw error;
  }
};
