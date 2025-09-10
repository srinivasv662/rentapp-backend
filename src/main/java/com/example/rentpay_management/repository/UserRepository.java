package com.example.rentpay_management.repository;

import com.example.rentpay_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(User.Role role);

    @Query("SELECT u.property.propertyId FROM User u WHERE u.id = :vendorId ")
    List<String> findPropertyIdsByVendorId(@Param("vendorId") Long vendorId);
}
