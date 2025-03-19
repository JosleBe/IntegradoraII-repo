package com.utez.integradora.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    private String emisorEmail;
    private String receptorEmail;
    private String texto;

}
