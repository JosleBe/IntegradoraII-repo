package com.utez.integradora.entity;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter@Getter@Data
@Document(collection = "campaign")
public class CampaignEntity {
    @Id
    private String id;
    private String name;
    private String description;
    private String image;
    private String location;
    private double progress;
    private double goal;


}

