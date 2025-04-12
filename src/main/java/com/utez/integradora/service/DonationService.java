package com.utez.integradora.service;

import com.utez.integradora.entity.ArticuloEntity;
import com.utez.integradora.entity.BeneficiaryEntity;
import com.utez.integradora.entity.DonationEntity;
import com.utez.integradora.entity.dto.ObjetoDto;
import com.utez.integradora.repository.CampaignRepository;
import com.utez.integradora.repository.DonationRepository;
import com.utez.integradora.repository.PreDonationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;
    private final CampaignRepository campaignRepository;
    private final PreDonationRepository preDonationRepository;
    public List<DonationEntity> getAllDonations() {
        return donationRepository.findAll();
    }

    public Optional<DonationEntity> getDonationById(String id) {
        return donationRepository.findById(id);
    }

    public List<DonationEntity> getDonationsByCampaign(String campaignId) {
        return donationRepository.findByCampaignId(campaignId);
    }

    public List<DonationEntity> getDonationsByDonor(String donorId) {
        return donationRepository.findByDonorId(donorId);
    }

    public DonationEntity saveDonation(DonationEntity donation) {
        return donationRepository.save(donation);
    }

    public void deleteDonation(String id) {
        donationRepository.deleteById(id);
    }
    public boolean hasDonatedToCampaign(String donorId, String campaignId) {
        // Aquí consultarías la base de datos para verificar si el donador ha donado a esta campaña
        return donationRepository.existsByDonorIdAndCampaignId(donorId, campaignId);
    }
    public DonationEntity donateInsumos(DonationEntity donation, List<ArticuloEntity> articulosDonados) throws Exception {

        // Obtener la campaña relacionada
        String campaignId = donation.getCampaignId();
        var campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new Exception("Campaña no encontrada"));

        // Obtener el objeto de la campaña, que contiene la lista de artículos
        ObjetoDto objetoDto = campaign.getObjeto();
        if (objetoDto == null || objetoDto.getArticulos() == null || objetoDto.getArticulos().isEmpty()) {
            throw new Exception("La campaña no tiene artículos disponibles.");
        }

        // Actualizar la cantidad de los artículos donados
        for (ArticuloEntity articuloDonado : articulosDonados) {
            // Buscar el artículo en la lista de la campaña
            ArticuloEntity articuloEnCampana = objetoDto.getArticulos().stream()
                    .filter(articulo -> articulo.getNombre().equalsIgnoreCase(articuloDonado.getNombre()))
                    .findFirst()
                    .orElse(null);

            if (articuloEnCampana != null) {
                // Descontar la cantidad donada
                int cantidadRestante = Math.max(0, articuloEnCampana.getCantidad() - articuloDonado.getCantidad());
                articuloEnCampana.setCantidad(cantidadRestante);
            }
        }
        // Actualizar la cantidad total de los artículos en la campaña
        int cantidadTotal = objetoDto.getArticulos().stream()
                .mapToInt(ArticuloEntity::getCantidad)
                .sum();

        // Asignar la fecha de donación y guardar la entidad de donación
        donation.setDonationDate(LocalDateTime.now());
        donationRepository.save(donation);
        campaignRepository.save(campaign);
        log.info("Donación guardada correctamente: {}", donation.getId());
        return donation;
    }
    public int getTotalInsumosDonadosPorCampana(String campaignId) {
        List<DonationEntity> donations = donationRepository.findByCampaignId(campaignId);

        return donations.stream()
                .filter(donation -> donation.getObject() != null && donation.getObject().getArticulos() != null)
                .flatMap(donation -> donation.getObject().getArticulos().stream())
                .mapToInt(ArticuloEntity::getCantidad)
                .sum();
    }
/*
    public DonationEntity approveDonation(String preDonationId) throws Exception {
        // Obtener la donación pendiente
        PreDonationDto preDonation = preDonationRepository.findById(preDonationId)
                .orElseThrow(() -> new Exception("Donación pendiente no encontrada"));

        if (!preDonation.isPending()) {
            throw new Exception("La donación ya ha sido procesada.");
        }

        // Obtener la campaña relacionada
        String campaignId = preDonation.getCampaignId();
        var campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new Exception("Campaña no encontrada"));

        // Obtener el objeto de la campaña, que contiene la lista de artículos
        ObjetoDto objetoDto = campaign.getObjeto();
        if (objetoDto == null || objetoDto.getArticulos() == null || objetoDto.getArticulos().isEmpty()) {
            throw new Exception("La campaña no tiene artículos disponibles.");
        }

        // Procesar los artículos donados para la donación aprobada
        for (ArticuloEntity articuloDonado : preDonation.getObject().getArticulos()) {
            // Buscar el artículo en la lista de la campaña
            ArticuloEntity articuloEnCampana = objetoDto.getArticulos().stream()
                    .filter(articulo -> articulo.getNombre().equalsIgnoreCase(articuloDonado.getNombre()))
                    .findFirst()
                    .orElse(null);

            if (articuloEnCampana != null) {
                // Descontar la cantidad donada
                int cantidadRestante = Math.max(0, articuloEnCampana.getCantidad() - articuloDonado.getCantidad());
                articuloEnCampana.setCantidad(cantidadRestante);
            }
        }

        // Actualizar la campaña con los artículos descontados
        campaignRepository.save(campaign);

        // Crear la entidad de donación definitiva
        DonationEntity donation = new DonationEntity();
        donation.setCampaignId(campaignId);
        donation.setDonorId(preDonation.getDonorId());
        donation.setAmount(preDonation.getAmount());
        donation.setEmail(preDonation.getEmail());
        donation.setPhone(preDonation.getPhone());
        donation.setName(preDonation.getName());
        donation.setDonationDate(LocalDateTime.now());

        // Guardar la donación final
        donationRepository.save(donation);

        // Marcar la donación como no pendiente
        preDonation.setPending(false);
        preDonationRepository.save(preDonation); // Actualizar la donación pendiente

        log.info("Donación aprobada y procesada correctamente: {}", preDonation.getId());

        return donation;
    }*/

    public boolean isDonorGeneral(String donorId) {
        List<DonationEntity> existDonor = donationRepository.findByDonorId(donorId);
        return !existDonor.isEmpty();
    }


}
