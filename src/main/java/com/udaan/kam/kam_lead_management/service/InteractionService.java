package com.udaan.kam.kam_lead_management.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udaan.kam.kam_lead_management.DTO.InteractionDTO;
import com.udaan.kam.kam_lead_management.entity.Contact;
import com.udaan.kam.kam_lead_management.entity.Interaction;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.entity.User;
import com.udaan.kam.kam_lead_management.exception.BadRequestException;
import com.udaan.kam.kam_lead_management.exception.InteractionNotFoundException;
import com.udaan.kam.kam_lead_management.repository.ContactRepository;
import com.udaan.kam.kam_lead_management.repository.InteractionRepository;
import com.udaan.kam.kam_lead_management.repository.UserRepository;
import com.udaan.kam.kam_lead_management.util.DTOConverterUtil;
import com.udaan.kam.kam_lead_management.util.PermissionUtils;

@Service
public class InteractionService {

	@Autowired
	private InteractionRepository interactionRepository;

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PermissionUtils permissionUtils;

	@Autowired
	private DTOConverterUtil dtoConverter;

	public List<InteractionDTO> getInteractionsByRestaurantId(Integer restaurantId, Integer requestingUserId) {
		if (!permissionUtils.isAdminOrAssignedManager(requestingUserId, restaurantId)) {
			throw new BadRequestException("Not authorized");
		}

		List<Interaction> interactions = interactionRepository.findByRestaurantId(restaurantId);

		return interactions.stream().map(interaction -> (dtoConverter.convertToInteractionDTO(interaction)))
				.collect(Collectors.toList());
	}

	public Interaction createInteraction(Integer restaurantId, Interaction interaction, Integer userId,
			Integer requestingUserId, Integer contactId) {
		Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

		// Check if the user is authorized
		if (!permissionUtils.isAdminOrAssignedManager(userId, restaurantId)) {
			throw new BadRequestException("You are not authorized to create interactions for this restaurant.");
		}

		User user = userRepository.findById(requestingUserId)
				.orElseThrow(() -> new BadRequestException("User not found with ID: " + requestingUserId));

		Contact contact = contactRepository.findById(contactId)
				.orElseThrow(() -> new BadRequestException("Contact not found with ID: " + contactId));

		interaction.setRestaurant(restaurant);
		interaction.setUser(user);
		interaction.setContact(contact);

		return interactionRepository.save(interaction);
	}

	public Interaction updateInteraction(Integer restaurantId, Integer interactionId, Integer userId,
			Interaction updatedInteraction) {
		Interaction interaction = interactionRepository.findById(interactionId)
				.orElseThrow(() -> new InteractionNotFoundException("Interaction not found with ID: " + interactionId));

		// Check if the user is authorized
		if (!permissionUtils.isAdminOrAssignedManager(restaurantId, userId)) {
			throw new BadRequestException("You are not authorized to update this interaction.");
		}

		interaction.setInteractionType(updatedInteraction.getInteractionType());
		interaction.setInteractionDate(updatedInteraction.getInteractionDate());
		interaction.setNotes(updatedInteraction.getNotes());
		interaction.setFollowUpDate(updatedInteraction.getFollowUpDate());

		return interactionRepository.save(interaction);
	}

	public void deleteInteraction(Integer restaurantId, Integer interactionId, Integer userId) {
		Interaction interaction = interactionRepository.findById(interactionId)
				.orElseThrow(() -> new InteractionNotFoundException("Interaction not found with ID: " + interactionId));

		// Check if the user is authorized
		if (!permissionUtils.isAdminOrAssignedManager(userId, restaurantId)) {
			throw new BadRequestException("You are not authorized to delete this interaction.");
		}

		interactionRepository.delete(interaction);
	}

}
