package com.utez.integradora.controller;

import com.utez.integradora.entity.DonationEntity;
import com.utez.integradora.entity.dto.DonationRequest;
import com.utez.integradora.entity.dto.ObjetoDto;
import com.utez.integradora.service.DonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @GetMapping
    public List<DonationEntity> getAllDonations() {
        return donationService.getAllDonations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonationEntity> getDonationById(@PathVariable String id) {
        Optional<DonationEntity> donation = donationService.getDonationById(id);
        return donation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/campaign/{campaignId}")
    public List<DonationEntity> getDonationsByCampaign(@PathVariable String campaignId) {
        log.info("Obteniendo donaciones", campaignId);

        return donationService.getDonationsByCampaign(campaignId);
    }

    @GetMapping("/donor/{donorId}")
    public List<DonationEntity> getDonationsByDonor(@PathVariable String donorId) {
        return donationService.getDonationsByDonor(donorId);
    }

    @PostMapping
    public DonationEntity createDonation(@RequestBody DonationEntity donation) {
        return donationService.saveDonation(donation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable String id) {
        donationService.deleteDonation(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/check-donor")
    public ResponseEntity<Map<String, Boolean>> checkDonorStatus(
            @RequestParam String campaignId,
            @RequestParam String donorId) {
        boolean hasDonated = donationService.hasDonatedToCampaign(donorId, campaignId);
        log.info("Obteniendo donaciones locas", campaignId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasDonated", hasDonated);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/insumo")
    public ResponseEntity<?> donateInsumos(@RequestBody DonationRequest donationRequest) {

        try {

            DonationEntity donation = new DonationEntity();
            donation.setCampaignId(donationRequest.getCampaignId());
            donation.setDonorId(donationRequest.getDonorId());
            donation.setAmount(donationRequest.getAmount());
            donation.setEmail(donationRequest.getEmail());
            donation.setPhone(donationRequest.getPhone());
            donation.setName(donationRequest.getName());

            // Asignar el objeto que contiene los artículos donados
            ObjetoDto objetoDto = new ObjetoDto();
            objetoDto.setArticulos(donationRequest.getDonaciones());  // Lista de artículos donados
            donation.setObject(objetoDto);
            log.info(donation.toString() );
            // Llamar al servicio para procesar la donación de insumos
            DonationEntity savedDonation = donationService.donateInsumos(donation, donationRequest.getDonaciones());

            // Responder con los datos actualizados
            return ResponseEntity.ok(savedDonation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/total-insumos/{campaignId}")
    public int getTotalInsumosDonados(@PathVariable String campaignId) {
        return donationService.getTotalInsumosDonadosPorCampana(campaignId);
    }


}
