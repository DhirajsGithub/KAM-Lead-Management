package com.udaan.kam.kam_lead_management.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.udaan.kam.kam_lead_management.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Query("SELECT r FROM Restaurant r WHERE " +
           "(:leadStatus IS NULL OR r.leadStatus = :leadStatus) AND " +
           "(:city IS NULL OR r.city = :city) AND " +
           "(:search IS NULL OR r.name LIKE %:search%)")
    Page<Restaurant> findByFiltersAndSearch(
            @Param("leadStatus") Restaurant.LeadStatus leadStatus,
            @Param("city") String city,
            @Param("search") String search,
            Pageable pageable);
    
    @Query("SELECT ru.restaurant FROM RestaurantUser ru WHERE ru.user.id = :userId")
    List<Restaurant> findRestaurantsByUserId(@Param("userId") Integer userId);
    
    @Query("SELECT r FROM Restaurant r WHERE r.id IN :ids " +
            "AND (:leadStatus IS NULL OR r.leadStatus = :leadStatus) " +
            "AND (:city IS NULL OR r.city = :city) " +
            "AND (:search IS NULL OR r.name LIKE %:search%)")
     Page<Restaurant> findByIdInAndFiltersAndSearch(
             @Param("ids") List<Integer> ids,
             @Param("leadStatus") Restaurant.LeadStatus leadStatus,
             @Param("city") String city,
             @Param("search") String search,
             Pageable pageable);
}