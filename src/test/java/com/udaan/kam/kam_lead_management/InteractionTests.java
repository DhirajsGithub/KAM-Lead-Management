package com.udaan.kam.kam_lead_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.udaan.kam.kam_lead_management.DTO.InteractionDTO;
import com.udaan.kam.kam_lead_management.entity.Contact;
import com.udaan.kam.kam_lead_management.entity.Interaction;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.entity.User;
import com.udaan.kam.kam_lead_management.exception.BadRequestException;
import com.udaan.kam.kam_lead_management.exception.InteractionNotFoundException;
import com.udaan.kam.kam_lead_management.repository.ContactRepository;
import com.udaan.kam.kam_lead_management.repository.InteractionRepository;
import com.udaan.kam.kam_lead_management.repository.RestaurantRepository;
import com.udaan.kam.kam_lead_management.repository.UserRepository;
import com.udaan.kam.kam_lead_management.service.InteractionService;
import com.udaan.kam.kam_lead_management.service.RestaurantService;
import com.udaan.kam.kam_lead_management.util.DTOConverterUtil;
import com.udaan.kam.kam_lead_management.util.PermissionUtils;

@ExtendWith(MockitoExtension.class)
class InteractionTests {

    @Mock
    private InteractionRepository interactionRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private PermissionUtils permissionUtils;
    
    @Mock
    private DTOConverterUtil dtoConverter;

    @InjectMocks
    private InteractionService interactionService;
    
    @Mock
    private RestaurantService restaurantService; 

    @Test
    void testGetInteractionsByRestaurantId_Success() {
        Integer restaurantId = 1;
        Integer userId = 101;

        Interaction interaction = new Interaction();
        interaction.setId(1);
        interaction.setInteractionType(Interaction.InteractionType.CALL);
        interaction.setInteractionDate(LocalDateTime.now());
        interaction.setNotes("Test interaction");

        InteractionDTO interactionDTO = new InteractionDTO();
        interactionDTO.setId(1);
        interactionDTO.setInteractionType("CALL");
        interactionDTO.setNotes("Test interaction");

        List<Interaction> interactions = List.of(interaction);

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);
        Mockito.when(interactionRepository.findByRestaurantId(restaurantId)).thenReturn(interactions);
        Mockito.when(dtoConverter.convertToInteractionDTO(interaction)).thenReturn(interactionDTO); // Mock the conversion

        List<InteractionDTO> result = interactionService.getInteractionsByRestaurantId(restaurantId, userId);

        assertEquals(1, result.size());
        assertEquals("CALL", result.get(0).getInteractionType());
        Mockito.verify(interactionRepository).findByRestaurantId(restaurantId);
        Mockito.verify(dtoConverter).convertToInteractionDTO(interaction); // Verify the conversion
    }

    @Test
    void testGetInteractionsByRestaurantId_Unauthorized() {
        Integer restaurantId = 1;
        Integer userId = 101;

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () ->
                interactionService.getInteractionsByRestaurantId(restaurantId, userId));

        Mockito.verify(interactionRepository, Mockito.never()).findByRestaurantId(Mockito.any());
    }

    @Test
    void testCreateInteraction_Success() {
        Integer restaurantId = 1;
        Integer userId = 101;
        Integer requestingUserId = 201;
        Integer contactId = 301;

        Interaction interaction = new Interaction();
        interaction.setInteractionType(Interaction.InteractionType.EMAIL);
        interaction.setInteractionDate(LocalDateTime.now());
        interaction.setNotes("Follow-up interaction");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        User user = new User();
        user.setId(requestingUserId);

        Contact contact = new Contact();
        contact.setId(contactId);

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);
        Mockito.when(restaurantService.getRestaurantById(restaurantId)).thenReturn(restaurant); // Mock behavior
        Mockito.when(userRepository.findById(requestingUserId)).thenReturn(Optional.of(user));
        Mockito.when(contactRepository.findById(contactId)).thenReturn(Optional.of(contact));
        Mockito.when(interactionRepository.save(interaction)).thenReturn(interaction);

        Interaction createdInteraction = interactionService.createInteraction(restaurantId, interaction, userId, requestingUserId, contactId);

        assertEquals(interaction, createdInteraction);
        Mockito.verify(interactionRepository).save(interaction);
    }

    @Test
    void testCreateInteraction_Unauthorized() {
        Integer restaurantId = 1;
        Integer userId = 101;
        Integer requestingUserId = 201;
        Integer contactId = 301;

        Interaction interaction = new Interaction();

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () ->
                interactionService.createInteraction(restaurantId, interaction, userId, requestingUserId, contactId));

        Mockito.verify(interactionRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testUpdateInteraction_Success() {
        Integer restaurantId = 1;
        Integer interactionId = 101;
        Integer userId = 201;

        Interaction existingInteraction = new Interaction();
        existingInteraction.setId(interactionId);

        Interaction updatedInteraction = new Interaction();
        updatedInteraction.setInteractionType(Interaction.InteractionType.MEETING);
        updatedInteraction.setInteractionDate(LocalDateTime.now());
        updatedInteraction.setNotes("Updated interaction");

        Mockito.when(interactionRepository.findById(interactionId)).thenReturn(Optional.of(existingInteraction));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(restaurantId, userId)).thenReturn(true); // Fixed here
        Mockito.when(interactionRepository.save(existingInteraction)).thenReturn(existingInteraction);

        Interaction result = interactionService.updateInteraction(restaurantId, interactionId, userId, updatedInteraction);

        assertEquals(existingInteraction, result);
        Mockito.verify(interactionRepository).save(existingInteraction);
    }


    @Test
    void testUpdateInteraction_Unauthorized() {
        Integer restaurantId = 1;
        Integer interactionId = 101;
        Integer userId = 201;

        Interaction updatedInteraction = new Interaction();

        Mockito.when(interactionRepository.findById(interactionId)).thenReturn(Optional.of(new Interaction()));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(restaurantId, userId)).thenReturn(false);

        assertThrows(BadRequestException.class, () ->
                interactionService.updateInteraction(restaurantId, interactionId, userId, updatedInteraction));

        Mockito.verify(interactionRepository, Mockito.never()).save(Mockito.any());
    }


    @Test
    void testDeleteInteraction_Success() {
        Integer restaurantId = 1;
        Integer interactionId = 101;
        Integer userId = 201;

        Interaction interaction = new Interaction();
        interaction.setId(interactionId);

        Mockito.when(interactionRepository.findById(interactionId)).thenReturn(Optional.of(interaction));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);

        interactionService.deleteInteraction(restaurantId, interactionId, userId);

        Mockito.verify(interactionRepository).delete(interaction);
    }

    @Test
    void testDeleteInteraction_Unauthorized() {
        Integer restaurantId = 1;
        Integer interactionId = 101;
        Integer userId = 201;

        Mockito.when(interactionRepository.findById(interactionId)).thenReturn(Optional.of(new Interaction()));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () ->
                interactionService.deleteInteraction(restaurantId, interactionId, userId));

        Mockito.verify(interactionRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void testDeleteInteraction_NotFound() {
        Integer restaurantId = 1;
        Integer interactionId = 101;
        Integer userId = 201;

        Mockito.when(interactionRepository.findById(interactionId)).thenReturn(Optional.empty());

        assertThrows(InteractionNotFoundException.class, () ->
                interactionService.deleteInteraction(restaurantId, interactionId, userId));

        Mockito.verify(interactionRepository, Mockito.never()).delete(Mockito.any());
    }
}
