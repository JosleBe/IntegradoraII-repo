package com.utez.integradora.entity;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contacts")
@Data
public class Contacto {
    @Id
    private String  id;
    @ManyToOne
    @JoinColumn(name = "usuario_id_receptor")
    private UserEntity receptor;

    @ManyToOne
    @JoinColumn(name = "usuario_id_emisor")
    private UserEntity emisor;
}