package com.utez.integradora.service;

import com.utez.integradora.entity.CampaignEntity;
import com.utez.integradora.entity.dto.CampaignDto;
import com.utez.integradora.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;

    public CampaignDto createCampaign(CampaignEntity campaignDto) {
        return toDto(campaignRepository.save(campaignDto));
    }
    public Optional<CampaignDto> getCampaignById(String id) {
        return campaignRepository.findById(id).map(this::toDto);
    }

    public Optional<CampaignDto> updateCampaign(String id, CampaignEntity campaignEntity) {
        return campaignRepository.findById(id).map(existingCampaign -> {
            existingCampaign.setName(campaignEntity.getName());
            existingCampaign.setDescription(campaignEntity.getDescription());
            existingCampaign.setImage(campaignEntity.getImage());
            existingCampaign.setLocation(campaignEntity.getLocation());
            existingCampaign.setProgress(campaignEntity.getProgress());
            existingCampaign.setGoal(campaignEntity.getGoal());
            return toDto(campaignRepository.save(existingCampaign));
        });
    }

    public void deleteCampaign(String id) {
        campaignRepository.deleteById(id);
    }
    public List<CampaignDto> getAllCampaigns() {
        return campaignRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public CampaignDto toDto(CampaignEntity campaignEntity) {
        return CampaignDto.builder()
                .id(campaignEntity.getId())
                .name(campaignEntity.getName())
                .description(campaignEntity.getDescription())
                .image(campaignEntity.getImage())
                .location(campaignEntity.getLocation())
                .progress(campaignEntity.getProgress())
                .goal(campaignEntity.getGoal())
                .build();
    }




}
