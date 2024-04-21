package com.reservationapp.repository;

import com.reservationapp.entity.User;
import com.reservationapp.paylaod.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
   Boolean existsByEmail(String email);
  Boolean existsByUsername(String username);

}