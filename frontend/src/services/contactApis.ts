import axios from 'axios';
import baseUrl from '../constants/baseUrl';

const API_BASE_URL = baseUrl+"/contacts";

// Create Contact API
export const createContact = async (
  contact: any,
  token: string,
  restaurantId: number // Path variable
) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/${restaurantId}`, contact, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting created contact details
  } catch (error) {
    console.error('Creating contact failed:', error);
    throw error;
  }
};


// Get All Contacts API
export const getAllContacts = async (token: string, restaurantId: number) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${restaurantId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting list of contacts
  } catch (error) {
    console.error('Fetching all contacts failed:', error);
    throw error;
  }
};

// Get Contact by ID API
export const getContactById = async (id: number, token: string, restaurantId: number) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${restaurantId}/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting contact details
  } catch (error) {
    console.error('Fetching contact by ID failed:', error);
    throw error;
  }
};

// Update Contact API
export const updateContact = async (
  id: number,
  updatedContact:any,
  token: string,
  restaurantId: number
) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/${restaurantId}/${id}`, updatedContact, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Expecting updated contact details
  } catch (error) {
    console.error('Updating contact failed:', error);
    throw error;
  }
};

// Delete Contact API
export const deleteContact = async (id: number, token: string, restaurantId: number) => {
  try {
    await axios.delete(`${API_BASE_URL}/${restaurantId}/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (error) {
    console.error('Deleting contact failed:', error);
    throw error;
  }
};
