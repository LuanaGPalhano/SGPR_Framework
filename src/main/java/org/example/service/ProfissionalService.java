package org.example.service;

import jakarta.transaction.Transactional;
import org.example.DTO.ProfissionalCadastroRequest;
import org.example.DTO.ProfissionalResponse;
import org.example.exception.DuplicateResourceException;
import org.example.exception.ErrorResponse;
import org.example.model.*;
import org.example.repository.PacienteRepository;
import org.example.repository.ProfissionalRepository;
import org.example.strategy.ProfissionalFactoryStrategy; // Import da Strategy
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfissionalService {

    private final ProfissionalRepository repository;
    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;

    // Lista onde o Spring injeta todas as implementações da interface Strategy
    private final List<ProfissionalFactoryStrategy> strategies;

    public ProfissionalService(ProfissionalRepository repository,
                               PacienteRepository pacienteRepository,
                               PasswordEncoder passwordEncoder,
                               List<ProfissionalFactoryStrategy> strategies) { // Injeção automática
        this.repository = repository;
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.strategies = strategies;
    }

    @Transactional
    public ProfissionalResponse cadastrar(ProfissionalCadastroRequest dados) {

        System.out.println("--- TENTATIVA DE CADASTRO (VIA STRATEGY) ---");

        if (dados.senha() == null || dados.senha().isEmpty()) {
            throw new IllegalArgumentException("A senha não pode ser vazia!");
        }

        if (repository.findByRegistroProfissional(dados.registroProfissional()).isPresent()) {
            throw new DuplicateResourceException("Registro Profissional já cadastrado!");
        }

        // --- PONTO VARIÁVEL (STRATEGY) ---
        // Busca a estratégia correta na lista baseada no tipo
        Profissional novoProfissional = strategies.stream()
                .filter(strategy -> strategy.supports(dados.tipoProfissional()))
                .findFirst()
                .map(strategy -> strategy.criarProfissional(dados))
                .orElseThrow(() -> new IllegalArgumentException("Tipo de profissional inválido ou não suportado: " + dados.tipoProfissional()));

        // --- PONTO FIXO (Preenchimento Comum) ---
        novoProfissional.setNome(dados.nome());
        novoProfissional.setEmail(dados.email());
        novoProfissional.setRegistroProfissional(dados.registroProfissional());
        novoProfissional.setSenha(passwordEncoder.encode(dados.senha()));

        Profissional salvo = repository.save(novoProfissional);

        return new ProfissionalResponse(salvo);
    }

    @Transactional
    public void associarPaciente(Long profissionalId, Long pacienteId) {
        Profissional profissional = repository.findById(profissionalId)
                .orElseThrow(() -> new ErrorResponse.ResourceNotFoundException("Profissional não encontrado."));

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ErrorResponse.ResourceNotFoundException("Paciente não encontrado."));

        profissional.getPacientes().add(paciente);
        repository.save(profissional);
    }
}