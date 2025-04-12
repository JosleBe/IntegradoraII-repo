package com.utez.integradora.controller;

import com.utez.integradora.entity.BeneficiaryEntity;
import com.utez.integradora.service.BeneficiaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {
    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping
    public List<BeneficiaryEntity> getAllBeneficiaries() {
        return beneficiaryService.getAllBeneficiaries();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficiaryEntity> getBeneficiaryById(@PathVariable String id) {
        Optional<BeneficiaryEntity> beneficiary = beneficiaryService.getBeneficiaryById(id);
        return beneficiary.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/campaign/{campaignId}")
    public List<BeneficiaryEntity> getBeneficiariesByCampaign(@PathVariable String campaignId) {
        return beneficiaryService.getBeneficiariesByCampaign(campaignId);
    }

    @GetMapping("/user/{beneficiaryId}")
    public List<BeneficiaryEntity> getBeneficiariesByUser(@PathVariable String beneficiaryId) {
        return beneficiaryService.getBeneficiariesByUser(beneficiaryId);
    }

    @PostMapping("/save")
    public BeneficiaryEntity createBeneficiary(@RequestBody BeneficiaryEntity beneficiary) {
        log.info("Hola");
        return beneficiaryService.saveBeneficiary(beneficiary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable String id) {

        beneficiaryService.deleteBeneficiary(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/check")
    public boolean checkIfBeneficiaryRegistered(@RequestParam String campaignId, @RequestParam String beneficiaryId) {
        return beneficiaryService.isBeneficiaryRegistered(campaignId, beneficiaryId);
    }
    @PostMapping("/check/beneficiary/{id}")
    public boolean checkIfBeneficiaryRegistered(@PathVariable String id) {
        return beneficiaryService.isBeneficiaryRegisteredGeneral(id);
    }
}
