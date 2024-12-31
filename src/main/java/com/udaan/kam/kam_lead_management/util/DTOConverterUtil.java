package com.udaan.kam.kam_lead_management.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.udaan.kam.kam_lead_management.DTO.InteractionDTO;
import com.udaan.kam.kam_lead_management.DTO.RestaurantDTO;
import com.udaan.kam.kam_lead_management.DTO.RestaurantDetailDTO;
import com.udaan.kam.kam_lead_management.DTO.UserDTO;
import com.udaan.kam.kam_lead_management.DTO.UserDetailDTO;
import com.udaan.kam.kam_lead_management.entity.CallSchedule;
import com.udaan.kam.kam_lead_management.entity.Contact;
import com.udaan.kam.kam_lead_management.entity.Interaction;
import com.udaan.kam.kam_lead_management.entity.Order;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.entity.RestaurantUser;
import com.udaan.kam.kam_lead_management.entity.User;

@Component
public class DTOConverterUtil {

	public UserDTO convertToUserDTO(User user) {
		if (user == null)
			return null;

		return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(),
				user.getRole().toString(), user.getIsActive());
	}

	public List<UserDTO> convertToUserDTOList(List<User> users) {
		if (users == null)
			return null;

		return users.stream().map(this::convertToUserDTO).collect(Collectors.toList());
	}

	public UserDetailDTO convertToUserDetailDto(User user) {
		List<RestaurantDTO> restaurantDTOs = user.getRestaurantUsers().stream()
				.map(restaurantUser -> convertToRestaurantDTO(restaurantUser.getRestaurant()))
				.collect(Collectors.toList());
		
		return new UserDetailDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(),
				user.getLastName(), user.getRole().toString(), user.getCreatedAt(), user.getIsActive(), restaurantDTOs,
				user.getInteractions().stream().map(interaction -> (convertToInteractionDTO(interaction)))
				.collect(Collectors.toList()), user.getPerformanceMetrices());
	}

	public RestaurantDTO convertToRestaurantDTO(Restaurant restaurant) {
		if (restaurant == null)
			return null;

		return new RestaurantDTO(restaurant.getId(), restaurant.getName(), restaurant.getAddress(),
				restaurant.getCity(), restaurant.getState(), restaurant.getPhone(), restaurant.getEmail(),
				restaurant.getCreatedAt(), restaurant.getLeadStatus().toString(), restaurant.getAnnualRevenue(),
				restaurant.getTimezone());
	}

	public RestaurantDetailDTO convertToRestaurantDetailDTO(Restaurant restaurant, List<Contact> contacts,
			List<CallSchedule> callSchedules, List<InteractionDTO> interactions, List<Order> orders) {
		if (restaurant == null)
			return null;

		List<UserDTO> userDTOs = restaurant.getRestaurantUsers().stream().map(RestaurantUser::getUser)
				.map(this::convertToUserDTO).collect(Collectors.toList());

		return new RestaurantDetailDTO(restaurant.getId(), restaurant.getName(), restaurant.getAddress(),
				restaurant.getCity(), restaurant.getState(), restaurant.getPhone(), restaurant.getEmail(),
				restaurant.getCreatedAt(), restaurant.getLeadStatus().toString(), restaurant.getAnnualRevenue(),
				restaurant.getTimezone(), userDTOs, contacts, callSchedules, interactions, orders);
	}

	public InteractionDTO convertToInteractionDTO(Interaction interaction) {

		return new InteractionDTO(interaction.getId(), interaction.getInteractionType().toString(),
				interaction.getInteractionDate(), interaction.getNotes(), interaction.getFollowUpDate(),
				interaction.getCreatedAt(), convertToUserDTO(interaction.getUser()), interaction.getContact());

	}

}