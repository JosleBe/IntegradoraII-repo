package com.utez.integradora.entity.dto;
import com.utez.integradora.entity.ArticuloEntity;
import lombok.Data;
import java.util.List;

@Data
public class ObjetoDto {
    private String tipo;
    private String cantidad;
    private List<ArticuloEntity> articulos;
}
