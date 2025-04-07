package com.utez.integradora.repository;

import com.utez.integradora.entity.dto.PreDonationDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public interface PreDonationRepository extends MongoRepository<PreDonationDto, String > {
}
