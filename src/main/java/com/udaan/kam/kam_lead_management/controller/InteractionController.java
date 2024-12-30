package com.udaan.kam.kam_lead_management.controller;

import com.udaan.kam.kam_lead_management.entity.Interaction;
import com.udaan.kam.kam_lead_management.security.UserDetailsImpl;
import com.udaan.kam.kam_lead_management.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController {

    @Autowired
    private InteractionService interactionService;

    @GetMapping("/{restaurantId}")
    public List<Interaction> getInteractionsByRestaurantId(@PathVariable Integer restaurantId,  @AuthenticationPrincipal UserDetailsImpl currentUser) {
    	Integer userId = currentUser.getUserId(); 
        return interactionService.getInteractionsByRestaurantId(restaurantId, userId);
    }

    @PostMapping("/{restaurantId}")
    public Interaction createInteraction(
            @PathVariable Integer restaurantId,
            @RequestParam Integer requestingUserId,
            @RequestParam Integer contactId,
            @RequestBody Interaction interaction, 
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
    	Integer userId = currentUser.getUserId(); 
        return interactionService.createInteraction(restaurantId, interaction, userId, requestingUserId, contactId);
    }

    @PutMapping("/{restaurantId}/{interactionId}")
    public Interaction updateInteraction(
    		 @PathVariable Integer restaurantId,
             @PathVariable Integer interactionId,
             @RequestBody Interaction updatedInteraction, 
             @AuthenticationPrincipal UserDetailsImpl currentUser) {
    	Integer userId = currentUser.getUserId(); 
        return interactionService.updateInteraction(restaurantId, interactionId, userId, updatedInteraction);
    }

    @DeleteMapping("/{restaurantId}/{interactionId}")
    public void deleteInteraction(
    		 @PathVariable Integer restaurantId,
             @PathVariable Integer interactionId,
             @AuthenticationPrincipal UserDetailsImpl currentUser) {
    	
    	Integer userId = currentUser.getUserId();	
    	
        interactionService.deleteInteraction(restaurantId, interactionId, userId);
    }
}
