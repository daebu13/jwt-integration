package com.reservationapp.service;

import com.reservationapp.entity.User;
import com.reservationapp.paylaod.LoginDto;
import com.reservationapp.paylaod.UserDto;
import com.reservationapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private JwtService jwtService;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public User addUser(UserDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(BCrypt.hashpw(dto.getPassword(),BCrypt.gensalt(10)));
        user.setUserRole(dto.getUserRole());
        user.setUsername(dto.getUsername());
        return userRepository.save(user);
    }

    public String verifyUser(LoginDto dto) {
        Optional<User> byUsername = userRepository.findByUsername(dto.getUsername());
        if (byUsername.isPresent()) {
            User user = byUsername.get();
             if (BCrypt.checkpw(dto.getPassword(), user.getPassword())){
                 return jwtService.generateToken(user);
             }

        }
        return null;


    }
}
