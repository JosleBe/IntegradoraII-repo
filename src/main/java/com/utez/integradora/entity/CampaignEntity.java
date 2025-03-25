package com.utez.integradora.entity;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter@Getter@Data
@Document(collection = "campaign")
public class CampaignEntity {
    @Id
    private String id;
    private String alignment;
    private String image;
    private String categoria;
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String meta;
    private String lugar;
    private String recursoTipo;
    private String  cantidad;
    private LocationEntity  location;
    private TemplateEntity templateEntity;
    private List<CommentEntity> comments;

}

