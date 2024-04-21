package com.reservationapp.controller;

import com.reservationapp.entity.User;
import com.reservationapp.paylaod.JwtResponse;
import com.reservationapp.paylaod.LoginDto;
import com.reservationapp.paylaod.UserDto;
import com.reservationapp.repository.UserRepository;
import com.reservationapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class AuthController {
    private UserService userService;
    private UserRepository userRepository;
    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }
    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody UserDto dto) {
        if(userRepository.existsByUsername(dto.getUsername())){
            return new ResponseEntity<>("Username already exists", HttpStatus.CONFLICT);
        }
        if(userRepository.existsByEmail(dto.getEmail())){
            return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
        }
        User user = userService.addUser(dto);
        if (user != null){
                return new ResponseEntity<>("sign up successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("/signIn")
    public ResponseEntity<?> verfyUser(@RequestBody LoginDto dto) {
        String jwtToken = userService.verifyUser(dto);
        if (jwtToken!=null) {
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setToken(jwtToken);
            return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/profile")
    public User getPrincipal(@AuthenticationPrincipal User user) {
        return user;
    }

}
