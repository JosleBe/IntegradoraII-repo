package com.utez.integradora.entity.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Data@Builder
public class CampaignDto {
    private String id;
    private String name;
    private String description;
    private String image;
    private String location;
    private double progress;
    private double goal;

}
