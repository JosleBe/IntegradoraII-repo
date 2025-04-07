package com.utez.integradora.entity;


import com.utez.integradora.entity.dto.ObjetoDto;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document( collection = "donations")
@Data
@Getter
@Setter
    public class DonationEntity {
        @Id
        private String id;
        private String campaignId;
        private String donorId;
        private double amount;
        private String email;
        private String phone;
        private String name;
        private ObjetoDto object;
        private LocalDateTime donationDate;
        private boolean isPending;
    }
