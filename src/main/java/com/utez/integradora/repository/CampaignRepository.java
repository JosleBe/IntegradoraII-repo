package com.utez.integradora.repository;

import com.utez.integradora.entity.CampaignEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends MongoRepository<CampaignEntity, String> {
}
