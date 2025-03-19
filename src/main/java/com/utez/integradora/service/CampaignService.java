package com.utez.integradora.service;

import com.utez.integradora.entity.CampaignEntity;
import com.utez.integradora.entity.LocationEntity;
import com.utez.integradora.entity.TemplateEntity;
import com.utez.integradora.entity.dto.CampaignDto;
import com.utez.integradora.repository.CampaignRepository;
import com.utez.integradora.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campanaRepository;


    private final  LocationRepository locationRepository;  // Dependencia para obtener la ubicación por id

    // Crear una nueva campaña
    public CampaignEntity crearCampana(CampaignDto campanaDTO) {
        log.info("Creando campana " + campanaDTO);
        CampaignEntity campana = new CampaignEntity();
        campana.setAlignment(campanaDTO.getAlignment());
        campana.setImage(campanaDTO.getImage());
        campana.setCategoria(campanaDTO.getCategoria());
        campana.setNombre(campanaDTO.getNombre());
        campana.setDescripcion(campanaDTO.getDescripcion());
        campana.setFechaInicio(campanaDTO.getFechaInicio());
        campana.setFechaFin(campanaDTO.getFechaFin());
        campana.setMeta(campanaDTO.getMeta());
        campana.setLugar(campanaDTO.getLugar());
        campana.setRecursoTipo(campanaDTO.getRecursoTipo());
        campana.setCantidad(campanaDTO.getCantidad());

        // Convertir la plantilla seleccionada (solo toma el codigo y nombre)
        TemplateEntity plantillaSeleccionada = new TemplateEntity();
        plantillaSeleccionada.setCodigo(campanaDTO.getPlantillaSeleccionada().getCodigo());
        plantillaSeleccionada.setNombre(campanaDTO.getPlantillaSeleccionada().getNombre());
        campana.setTemplateEntity(plantillaSeleccionada);
        LocationEntity location = locationRepository.findById(campanaDTO.getLocation().getId()).orElse(null);
        campana.setLocation(location);

        return campanaRepository.save(campana);
    }
    public List<CampaignEntity> getAllCampaigns() {
        log.info("Listando todas las campanas");
        return campanaRepository.findAll(); // Retorna todas las campañas almacenadas
    }
}
