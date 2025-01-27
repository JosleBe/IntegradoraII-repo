package com.utez.integradora.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class FacebookUserResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String accessToken;
}
