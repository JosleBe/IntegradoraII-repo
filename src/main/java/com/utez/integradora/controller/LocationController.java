package com.utez.integradora.controller;


import com.utez.integradora.entity.LocationEntity;
import com.utez.integradora.entity.dto.ApiResponse;
import com.utez.integradora.entity.dto.LocationDto;
import com.utez.integradora.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {
    public final LocationService ubicationService;

    @PostMapping("/save")
    private ResponseEntity<ApiResponse> save (@RequestBody LocationDto dtoUbication){

       LocationEntity ubicationEntity = ubicationService.save(dtoUbication);
        if (ubicationEntity == null) {
            System.out.println(dtoUbication);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Error al guardar ubicación", null));
        }


        return ResponseEntity.ok().body(new ApiResponse(200, "Ubicacion registrada correctamente",  ubicationEntity));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LocationEntity>> getAll(){
        return ResponseEntity.ok().body(ubicationService.findAll());
    }
}
