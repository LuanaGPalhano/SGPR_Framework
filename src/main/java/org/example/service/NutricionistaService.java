package org.example.service;

import org.example.DTO.DietaCadastroRequest; // Se já tiver
import org.example.DTO.DietaResponse;       // Se já tiver
import org.example.model.Dieta;
import org.example.repository.DietaRepository;
import org.example.repository.NutricionistaRepository;
import org.springframework.stereotype.Service;

@Service
public class NutricionistaService {

    private final DietaRepository dietaRepository;
    private final NutricionistaRepository nutricionistaRepository;

    public NutricionistaService(DietaRepository dietaRepository,
                                NutricionistaRepository nutricionistaRepository) {
        this.dietaRepository = dietaRepository;
        this.nutricionistaRepository = nutricionistaRepository;
    }

}