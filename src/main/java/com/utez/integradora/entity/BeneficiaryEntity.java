package com.utez.integradora.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Document( collection = "beneficiaries")
@Data
@Getter
@Setter
public class BeneficiaryEntity {
    @Id
    private String id;
    private String campaignId;
    private String beneficiaryId;
    private String email;
    private String phone;
    private String name;
    private LocalDateTime joinDate;
}
