package com.utez.integradora.controller;

import com.utez.integradora.entity.BeneficiaryEntity;
import com.utez.integradora.entity.UserEntity;
import com.utez.integradora.repository.UserRepository;
import com.utez.integradora.repository.UserRepository;
import com.utez.integradora.service.BeneficiaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;
    private final UserRepository usuarioRepository;

    public BeneficiaryController(BeneficiaryService beneficiaryService, UserRepository usuarioRepository) {
        this.beneficiaryService = beneficiaryService;
        this.usuarioRepository = usuarioRepository;
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

    // ✅ NUEVO PATCH: habilitar usuario
    @PatchMapping("/enable-user/{id}")
    public ResponseEntity<String> enableUser(@PathVariable String id) {
        Optional<UserEntity> userOptional = usuarioRepository.findById(id);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setActive(true);
            usuarioRepository.save(user);
            return ResponseEntity.ok("Usuario habilitado exitosamente.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
    }
    @PatchMapping("/toggle-status/{id}")
    public ResponseEntity<?> toggleStatus(@PathVariable String id) {
        Optional<BeneficiaryEntity> optional = beneficiaryService.getBeneficiaryById(id);
        if (optional.isPresent()) {
            BeneficiaryEntity beneficiary = optional.get();
            beneficiary.setActive(!beneficiary.isActive());
            beneficiaryService.saveBeneficiary(beneficiary);

            // Si usas WebSocket, puedes notificar así:
            // messagingTemplate.convertAndSend("/topic/beneficiarios", "actualizar");

            return ResponseEntity.ok("Estado del beneficiario actualizado.");
        }
        return ResponseEntity.notFound().build();
    }
}
