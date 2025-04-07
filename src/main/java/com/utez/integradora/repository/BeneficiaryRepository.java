package com.utez.integradora.repository;

import com.utez.integradora.entity.BeneficiaryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface BeneficiaryRepository extends MongoRepository<BeneficiaryEntity, String> {
    List<BeneficiaryEntity> findByCampaignId(String campaignId);
    List<BeneficiaryEntity> findByBeneficiaryId(String beneficiaryId);
    Optional<BeneficiaryEntity> findByCampaignIdAndBeneficiaryId(String campaignId, String beneficiaryId);
}