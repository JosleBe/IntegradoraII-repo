package com.utez.integradora.service;


import com.utez.integradora.entity.ArticuloEntity;
import com.utez.integradora.entity.DonationEntity;
import com.utez.integradora.entity.dto.PreDonationDto;
import com.utez.integradora.repository.CampaignRepository;
import com.utez.integradora.repository.DonationRepository;
import com.utez.integradora.repository.PreDonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreDonationService {
    private final PreDonationRepository preDonationRepository;
    private final DonationRepository donationRepository;
    private final CampaignRepository campaignRepository;



    public PreDonationDto savePreDonation(PreDonationDto preDonation) {
        return preDonationRepository.save(preDonation);
    }

    /**
     * Elimina una pre-donación por ID.
     *
     * @param id El ID de la pre-donación a eliminar.
     * @return true si se eliminó correctamente, false si no se encontró.
     */
    public boolean removePreDonation(String id) {
        Optional<PreDonationDto> preDonation = preDonationRepository.findById(id);

        if (preDonation.isPresent()) {
            preDonationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<PreDonationDto> getPendingDonations() {
        // Listar todas las donaciones que están pendientes de aprobación
        return preDonationRepository.findAll().stream()
                .filter(PreDonationDto::isPending) // Filtrar las donaciones pendientes
                .collect(Collectors.toList());
    }
}
