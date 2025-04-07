package com.utez.integradora.repository;

import com.utez.integradora.entity.DonationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DonationRepository extends MongoRepository<DonationEntity, String> {
    List<DonationEntity> findByCampaignId(String campaignId);
    List<DonationEntity> findByDonorId(String donorId);
    boolean existsByDonorIdAndCampaignId(String donorId, String campaignId);
}
