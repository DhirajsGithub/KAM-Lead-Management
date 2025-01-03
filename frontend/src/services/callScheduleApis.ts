import axios from 'axios';
import baseUrl from '../constants/baseUrl';

const API_BASE_URL = baseUrl + "/call-schedules";

// Create CallSchedule API
export const createCallSchedule = async (
  callSchedule: any,
  token: string,
  restaurantId: number // Path variable
) => {
    console.log(callSchedule)
  try {
    const response = await axios.post(`${API_BASE_URL}/${restaurantId}`, callSchedule, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting created call schedule details
  } catch (error) {
    console.error('Creating call schedule failed:', error);
    throw error;
  }
};

// Get All CallSchedules API
export const getAllCallSchedules = async (token: string, restaurantId: number) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${restaurantId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting list of call schedules
  } catch (error) {
    console.error('Fetching all call schedules failed:', error);
    throw error;
  }
};

// Update CallSchedule API
export const updateCallSchedule = async (
  id: number,
  updatedCallSchedule: any,
  token: string,
  restaurantId: number
) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/${restaurantId}/${id}`, updatedCallSchedule, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting updated call schedule details
  } catch (error) {
    console.error('Updating call schedule failed:', error);
    throw error;
  }
};

// Delete CallSchedule API
export const deleteCallSchedule = async (id: number, token: string, restaurantId: number) => {
  try {
    await axios.delete(`${API_BASE_URL}/${restaurantId}/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (error) {
    console.error('Deleting call schedule failed:', error);
    throw error;
  }
};
