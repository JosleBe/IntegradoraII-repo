package com.utez.integradora.entity.dto;

import com.utez.integradora.entity.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private String id;
    private String address;
    private Coordinates coordinates;
    private String city;
    private String state;
    private String country;
}
