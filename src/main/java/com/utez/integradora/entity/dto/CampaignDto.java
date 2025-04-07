package com.utez.integradora.entity.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Data@Builder
public class CampaignDto {
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
    private String cantidad;
    private String locationId;  // Solo el ID de la ubicación
    private TemplateDto plantillaSeleccionada;
    private LocationDto location;
    private ObjetoDto objeto;
    private boolean estado;

}
