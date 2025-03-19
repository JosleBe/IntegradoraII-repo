package com.utez.integradora.entity;


import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "locations")
@Data
@Builder
@Getter
@Setter
public class LocationEntity {
    @Id
    private String id;
    private String address;
    private Coordinates coordinates;
    private String city;
    private String state;
    private String country;
}
