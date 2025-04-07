package com.utez.integradora.entity.dto;


import com.utez.integradora.entity.ArticuloEntity;
import lombok.Data;

import java.util.List;

@Data
public class DonationRequest {
    private String campaignId;
    private String donorId;
    private double amount;
    private String email;
    private String phone;
    private String name;
    private List<ArticuloEntity> donaciones;  // Lista de artículos donados
}
