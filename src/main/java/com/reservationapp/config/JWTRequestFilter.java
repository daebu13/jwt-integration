package com.reservationapp.config;

import com.reservationapp.entity.User;
import com.reservationapp.repository.UserRepository;
import com.reservationapp.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    private UserRepository userRepository;

    public JWTRequestFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if(tokenHeader!=null && tokenHeader.startsWith("Bearer ")){
            String token = tokenHeader.substring(8,tokenHeader.length()-1);
            String usernameFromToken = jwtService.getUsernameFromToken(token);
            Optional<User> byUsername = userRepository.findByUsername(usernameFromToken);
            if (byUsername.isPresent()) {
                User user = byUsername.get();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())));
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }


        filterChain.doFilter(request,response);
    }
}
