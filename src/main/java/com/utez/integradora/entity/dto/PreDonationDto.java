package com.utez.integradora.entity.dto;

import com.utez.integradora.entity.ArticuloEntity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@Document(collection = "preDonation")
public class PreDonationDto {
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
