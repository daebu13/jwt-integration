package com.reservationapp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.reservationapp.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt-secret}")
    private String algorithmKey;
    @Value("${jwt-issuer}")
    private String issuer;
    @Value("${jwt-expiryDuration}")
    private int expiryTime;


    private Algorithm algorithm;

    private final static String USER_NAME = "username";

    @PostConstruct
    public void postConstruct(){
        algorithm = Algorithm.HMAC256(algorithmKey);
    }


    public String generateToken(User user){
        return  JWT.create()
                .withClaim(USER_NAME,user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+expiryTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getUsernameFromToken(String token){
        DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return decodedJWT.getClaim(USER_NAME).asString();
    }
}
