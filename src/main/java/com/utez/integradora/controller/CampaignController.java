package com.utez.integradora.controller;

import com.utez.integradora.entity.CampaignEntity;
import com.utez.integradora.entity.CommentEntity;
import com.utez.integradora.entity.dto.ApiResponse;
import com.utez.integradora.entity.dto.CampaignDto;
import com.utez.integradora.entity.dto.ReqRes;
import com.utez.integradora.service.CampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/campaign")
public class CampaignController {

    private final CampaignService campanaService;
    private final SimpMessagingTemplate messagingTemplate;
    // Endpoint para crear una nueva campaña
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> crearCampana(@RequestBody CampaignDto campanaDTO) {
        log.info("Creando campana " + campanaDTO);
        try {
            // Llamamos al servicio para crear la campaña
            CampaignEntity campana = campanaService.crearCampana(campanaDTO);

            // Retornamos la campaña creada con un código de estado HTTP 201 (Creado)
            ApiResponse response = ApiResponse.success("Campaña creada exitosamente", campana);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            // Si hay un error, devolvemos un código de error 500 con el mensaje de error
            ApiResponse response = ApiResponse.error("Error al crear la campaña: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public ResponseEntity<ApiResponse> getAllCampaigns() {
        log.info("Listando todas las campanas");
        try {
            List<CampaignEntity> campaigns = campanaService.getAllCampaigns();
            log.info("hola");
            if (campaigns.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse(404, "No se encontraron campañas", null));
            }
            return ResponseEntity.ok(new ApiResponse(200, "Campañas obtenidas con éxito", campaigns));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(500, e.getMessage(), null));
        }
    }
    @GetMapping("/{campaignId}/comments")
    public List<CommentEntity> getComments(@PathVariable String campaignId) {
        return campanaService.getCommentsByCampaignId(campaignId);
    }
    @PostMapping("/{campaignId}/comments")

    public CommentEntity addComment(@PathVariable String campaignId, @RequestBody CommentEntity comment) {
        log.info("comentario");
        CommentEntity commentEntity = campanaService.addComment(campaignId, comment.getAutor(), comment.getTexto(), comment.getImagen());
        messagingTemplate.convertAndSend("/topic/campaign/" +campaignId , commentEntity);
        return commentEntity;
    }
}
