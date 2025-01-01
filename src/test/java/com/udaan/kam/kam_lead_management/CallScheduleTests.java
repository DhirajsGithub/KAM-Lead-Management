package com.udaan.kam.kam_lead_management;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.udaan.kam.kam_lead_management.entity.CallSchedule;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.exception.BadRequestException;
import com.udaan.kam.kam_lead_management.exception.CallScheduleNotFoundException;
import com.udaan.kam.kam_lead_management.repository.CallScheduleRepository;
import com.udaan.kam.kam_lead_management.repository.RestaurantRepository;
import com.udaan.kam.kam_lead_management.service.CallScheduleService;
import com.udaan.kam.kam_lead_management.util.PermissionUtils;

@ExtendWith(MockitoExtension.class)
public class CallScheduleTests {

    @Mock
    private CallScheduleRepository callScheduleRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private PermissionUtils permissionUtils;

    @InjectMocks
    private CallScheduleService callScheduleService;

    private CallSchedule sampleSchedule;
    private Restaurant sampleRestaurant;

    @BeforeEach
    void setUp() {
        sampleRestaurant = new Restaurant();
        sampleRestaurant.setId(1);
        sampleRestaurant.setName("Test Restaurant");

        sampleSchedule = new CallSchedule();
        sampleSchedule.setId(1);
        sampleSchedule.setRestaurant(sampleRestaurant);
        sampleSchedule.setFrequencyDays(7);
        sampleSchedule.setPriorityLevel(3);
        sampleSchedule.setLastCallDate(LocalDateTime.now().minusDays(7).toString());
        sampleSchedule.setNextCallDate(LocalDateTime.now().toString());
    }

    // Unit Tests

    @Test
    void testCreateCallSchedule_Success() {
        when(permissionUtils.isAdminOrAssignedManager(anyInt(), anyInt())).thenReturn(true);
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(sampleRestaurant));
        when(callScheduleRepository.save(any(CallSchedule.class))).thenReturn(sampleSchedule);

        CallSchedule result = callScheduleService.createCallSchedule(sampleSchedule, 1, 1);

        assertNotNull(result);
        assertEquals(sampleSchedule.getFrequencyDays(), result.getFrequencyDays());
        verify(callScheduleRepository, times(1)).save(sampleSchedule);
    }

    @Test
    void testCreateCallSchedule_Unauthorized() {
        when(permissionUtils.isAdminOrAssignedManager(anyInt(), anyInt())).thenReturn(false);

        Exception exception = assertThrows(BadRequestException.class, () -> {
            callScheduleService.createCallSchedule(sampleSchedule, 1, 1);
        });

        assertTrue(exception.getMessage().contains("You are not authorized"));
        verify(callScheduleRepository, times(0)).save(any(CallSchedule.class));
    }

    @Test
    void testGetCallSchedulesByRestaurantId_Success() {
        when(permissionUtils.isAdminOrAssignedManager(anyInt(), anyInt())).thenReturn(true);
        when(callScheduleRepository.findByRestaurantId(1)).thenReturn(List.of(sampleSchedule));

        List<CallSchedule> schedules = callScheduleService.getCallSchedulesByRestaurantId(1, 1);

        assertNotNull(schedules);
        assertFalse(schedules.isEmpty());
        assertEquals(1, schedules.size());
        verify(callScheduleRepository, times(1)).findByRestaurantId(1);
    }

    @Test
    void testGetCallSchedulesByRestaurantId_Unauthorized() {
        when(permissionUtils.isAdminOrAssignedManager(anyInt(), anyInt())).thenReturn(false);

        Exception exception = assertThrows(BadRequestException.class, () -> {
            callScheduleService.getCallSchedulesByRestaurantId(1, 1);
        });

        assertTrue(exception.getMessage().contains("You are not authorized"));
        verify(callScheduleRepository, times(0)).findByRestaurantId(1);
    }

    @Test
    void testUpdateCallSchedule_Success() {
        when(callScheduleRepository.findById(1)).thenReturn(Optional.of(sampleSchedule));
        when(permissionUtils.isAdminOrAssignedManager(anyInt(), anyInt())).thenReturn(true);
        when(callScheduleRepository.save(any(CallSchedule.class))).thenReturn(sampleSchedule);

        CallSchedule updated = callScheduleService.updateCallSchedule(1, sampleSchedule, 1, 1);

        assertNotNull(updated);
        assertEquals(sampleSchedule.getFrequencyDays(), updated.getFrequencyDays());
        verify(callScheduleRepository, times(1)).save(sampleSchedule);
    }

    @Test
    void testUpdateCallSchedule_NotFound() {
        when(callScheduleRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CallScheduleNotFoundException.class, () -> {
            callScheduleService.updateCallSchedule(1, sampleSchedule, 1, 1);
        });

        assertTrue(exception.getMessage().contains("Call schedule not found"));
        verify(callScheduleRepository, times(0)).save(any(CallSchedule.class));
    }

    @Test
    void testDeleteCallSchedule_Success() {
        when(callScheduleRepository.findById(1)).thenReturn(Optional.of(sampleSchedule));
        when(permissionUtils.isAdminOrAssignedManager(anyInt(), anyInt())).thenReturn(true);

        assertDoesNotThrow(() -> callScheduleService.deleteCallSchedule(1, 1, 1));

        verify(callScheduleRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteCallSchedule_NotFound() {
        when(callScheduleRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CallScheduleNotFoundException.class, () -> {
            callScheduleService.deleteCallSchedule(1, 1, 1);
        });

        assertTrue(exception.getMessage().contains("Call schedule not found"));
        verify(callScheduleRepository, times(0)).deleteById(anyInt());
    }

    // Integration Test Example

    @Test
    void testIntegration_CreateAndRetrieveCallSchedule() {
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(sampleRestaurant));

        when(permissionUtils.isAdminOrAssignedManager(anyInt(), anyInt())).thenReturn(true);

        when(callScheduleRepository.save(any(CallSchedule.class))).thenReturn(sampleSchedule);

        when(callScheduleRepository.findById(1)).thenReturn(Optional.of(sampleSchedule));

        CallSchedule createdSchedule = callScheduleService.createCallSchedule(sampleSchedule, 1, 1);

        Optional<CallSchedule> retrievedSchedule = callScheduleRepository.findById(createdSchedule.getId());

        assertTrue(retrievedSchedule.isPresent());
        assertEquals(createdSchedule.getFrequencyDays(), retrievedSchedule.get().getFrequencyDays());
    }

}
