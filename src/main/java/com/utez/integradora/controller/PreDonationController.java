package com.utez.integradora.controller;


import com.utez.integradora.entity.ArticuloEntity;
import com.utez.integradora.entity.DonationEntity;
import com.utez.integradora.entity.dto.DonationRequest;
import com.utez.integradora.entity.dto.ObjetoDto;
import com.utez.integradora.entity.dto.PreDonationDto;
import com.utez.integradora.service.DonationService;
import com.utez.integradora.service.PreDonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pre-donation")
public class PreDonationController {
    private final PreDonationService donationService;
    private final DonationService donation;
    // 1. Guardar una donación como pendiente

    @PostMapping("/pre-donate")
    public ResponseEntity<?> donateInsumos(@RequestBody DonationRequest donationRequest) {

        try {
            // Crear la donación pendiente (PreDonationDto)
            PreDonationDto preDonation = new PreDonationDto();
            preDonation.setCampaignId(donationRequest.getCampaignId());
            preDonation.setDonorId(donationRequest.getDonorId());
            preDonation.setAmount(donationRequest.getAmount());
            preDonation.setEmail(donationRequest.getEmail());
            preDonation.setPhone(donationRequest.getPhone());
            preDonation.setName(donationRequest.getName());
            ObjetoDto objetoDto = new ObjetoDto();
            objetoDto.setArticulos(donationRequest.getDonaciones());   // Asignar objeto con los artículos donados
            preDonation.setPending(true);  // Establecer que la donación está pendiente
            preDonation.setObject(objetoDto);
            // Guardar la donación como pendiente en el repositorio
            PreDonationDto savedPreDonation = donationService.savePreDonation(preDonation);
            return ResponseEntity.ok(savedPreDonation);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al guardar la donación pendiente: " + e.getMessage());
        }
    }
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deletePreDonationById(@PathVariable("id") String id) {
        log.info("Borrando pre-donación con ID: {}", id);

        if (donationService.removePreDonation(id)) {
            return ResponseEntity.ok().body(Map.of("message", "Pre-donación eliminada correctamente", "id", id));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No se encontró la pre-donación con ID " + id));
        }
    }
    // 2. Listar todas las donaciones pendientes
    @GetMapping("/pending")
    public ResponseEntity<List<PreDonationDto>> getPendingDonations() {
        try {
            List<PreDonationDto> pendingDonations = donationService.getPendingDonations();
            return ResponseEntity.ok(pendingDonations);  // Retorna las donaciones pendientes
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Manejo de errores
        }
    }
/*
    // 3. Aprobar una donación pendiente
    @PutMapping("/approve/{id}")
    public ResponseEntity<DonationEntity> approveDonation(@PathVariable String id) {
        try {
            DonationEntity approvedDonation = donation.approveDonation(id);
            return ResponseEntity.ok(approvedDonation);  // Retorna la donación ya procesada
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Manejo de errores
        }
    }*/
}
