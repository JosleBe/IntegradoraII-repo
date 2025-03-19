package com.utez.integradora.service;

import com.utez.integradora.entity.LocationEntity;
import com.utez.integradora.entity.dto.LocationDto;
import com.utez.integradora.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
private final LocationRepository ubicationRepository;

public LocationEntity save(LocationDto dtoUbication) {
    LocationEntity ubicationEntity = LocationEntity.builder()
            .state(dtoUbication.getState())
            .address(dtoUbication.getAddress()  )
            .city(dtoUbication.getCity())
            .coordinates(dtoUbication.getCoordinates())
            .country(dtoUbication.getCountry())
            .build();
    return ubicationRepository.save(ubicationEntity);
 }

 public List<LocationEntity> findAll() {
    return ubicationRepository.findAll();
 }
 }

