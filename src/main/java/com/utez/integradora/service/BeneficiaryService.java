package com.utez.integradora.service;

import com.utez.integradora.entity.BeneficiaryEntity;
import com.utez.integradora.repository.BeneficiaryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BeneficiaryService {
    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public List<BeneficiaryEntity> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    public Optional<BeneficiaryEntity> getBeneficiaryById(String id) {
        return beneficiaryRepository.findById(id);
    }

    public List<BeneficiaryEntity> getBeneficiariesByCampaign(String campaignId) {
        return beneficiaryRepository.findByCampaignId(campaignId);
    }

    public List<BeneficiaryEntity> getBeneficiariesByUser(String beneficiaryId) {
        return beneficiaryRepository.findByBeneficiaryId(beneficiaryId);
    }

    public BeneficiaryEntity saveBeneficiary(BeneficiaryEntity beneficiary) {
        beneficiary.setJoinDate(LocalDateTime.now());
        return beneficiaryRepository.save(beneficiary);
    }
    public boolean isBeneficiaryRegistered(String campaignId, String beneficiaryId) {
        Optional<BeneficiaryEntity> existingBeneficiary = beneficiaryRepository.findByCampaignIdAndBeneficiaryId(campaignId, beneficiaryId);
        return existingBeneficiary.isPresent();
    }
    public void deleteBeneficiary(String id) {
        beneficiaryRepository.deleteById(id);
    }
}
