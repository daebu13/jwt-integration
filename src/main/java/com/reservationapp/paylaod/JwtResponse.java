package com.reservationapp.paylaod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class JwtResponse {
    private String token;

    private String tokenType="bearer";
}
