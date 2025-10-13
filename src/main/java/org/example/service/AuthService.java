package org.example.service;

import org.example.DTO.LoginRequest;
import org.example.DTO.LoginResponse;
import org.example.model.Nutricionista;
import org.example.model.Paciente;
import org.example.repository.NutricionistaRepository;
import org.example.repository.PacienteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    // 1. Campos para guardar os objetos dos repositórios e do encoder
    private final PacienteRepository pacienteRepository;
    private final NutricionistaRepository nutricionistaRepository;
    private final PasswordEncoder passwordEncoder;

    // 2. Construtor que recebe os objetos prontos do Spring (Injeção de Dependência)
    public AuthService(PacienteRepository pacienteRepository, NutricionistaRepository nutricionistaRepository, PasswordEncoder passwordEncoder) {
        this.pacienteRepository = pacienteRepository;
        this.nutricionistaRepository = nutricionistaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<LoginResponse> autenticar(LoginRequest request) {

        Optional<Nutricionista> nutricionistaOpt = this.nutricionistaRepository.findByCrnUf(request.login());
        if (nutricionistaOpt.isPresent() && passwordEncoder.matches(request.senha(), nutricionistaOpt.get().getSenha())) {
            return Optional.of(new LoginResponse(nutricionistaOpt.get().getCrnUf(), "NUTRICIONISTA"));
        }

        // 3. Uso do método no objeto 'pacienteRepository'
        Optional<Paciente> pacienteOpt = this.pacienteRepository.findByCpf(request.login());
        if (pacienteOpt.isPresent() && passwordEncoder.matches(request.senha(), pacienteOpt.get().getSenha())) {
            return Optional.of(new LoginResponse(pacienteOpt.get().getCpf(), "PACIENTE"));
        }

        return Optional.empty();
    }
}
