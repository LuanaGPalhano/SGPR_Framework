package org.example.service;

import jakarta.transaction.Transactional;
import org.example.DTO.ProfissionalCadastroRequest;
import org.example.DTO.ProfissionalResponse;
import org.example.exception.DuplicateResourceException;
import org.example.exception.ErrorResponse;
import org.example.model.*;
import org.example.repository.PacienteRepository;
import org.example.repository.ProfissionalRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfissionalService {

    private final ProfissionalRepository repository;
    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfissionalService(ProfissionalRepository repository,
                               PacienteRepository pacienteRepository,
                               PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- Método de Cadastro (CORRIGIDO) ---
    @Transactional // Importante para garantir que salve nas duas tabelas (pai e filho)
    public ProfissionalResponse cadastrar(ProfissionalCadastroRequest dados) {

        // DEBUG: Verificar se os dados chegaram
        System.out.println("--- TENTATIVA DE CADASTRO ---");
        System.out.println("Nome recebido: " + dados.nome());
        System.out.println("Email recebido: " + dados.email());
        System.out.println("Senha recebida: " + dados.senha());

        if (dados.senha() == null || dados.senha().isEmpty()) {
            throw new IllegalArgumentException("A senha não pode ser vazia!");
        }
        // Verifica se já existe alguém com esse registro (CRN, CREF, CRP)
        if (repository.findByRegistroProfissional(dados.registroProfissional()).isPresent()) {
            throw new DuplicateResourceException("Registro Profissional já cadastrado!");
        }

        Profissional novoProfissional;

        // Factory: Instancia a classe correta E preenche o campo específico da filha
        switch (dados.tipoProfissional().toUpperCase()) {
            case "NUTRICIONISTA" -> {
                Nutricionista n = new Nutricionista();
                // Importante: Preencher o campo específico da tabela 'nutricionistas'
                // Verifique se no seu Model o nome é 'setCrn' ou 'setCrnUf'
                n.setCrn(dados.registroProfissional());
                novoProfissional = n;
            }
            case "EDUCADOR_FISICO" -> {
                EducadorFisico e = new EducadorFisico();
                // Preencher o campo específico da tabela 'educadores_fisicos'
                e.setCref(dados.registroProfissional());
                novoProfissional = e;
            }
            case "PSICOLOGO" -> {
                Psicologo p = new Psicologo();
                // Preencher o campo específico da tabela 'psicologos'
                p.setCrp(dados.registroProfissional());
                novoProfissional = p;
            }
            default -> throw new IllegalArgumentException("Tipo de profissional inválido: " + dados.tipoProfissional());
        }

        // Preenche os dados FIXOS (Tabela 'profissionais')
        novoProfissional.setNome(dados.nome());
        novoProfissional.setEmail(dados.email());
        // Preenche o registro TAMBÉM na tabela pai para facilitar o login
        novoProfissional.setRegistroProfissional(dados.registroProfissional());
        novoProfissional.setSenha(passwordEncoder.encode(dados.senha()));

        // O JPA é inteligente: ele salva na tabela 'profissionais' E na tabela específica
        Profissional salvo = repository.save(novoProfissional);

        return new ProfissionalResponse(salvo);
    }

    // --- Método de Associação (Ponto Fixo) ---
    @Transactional
    public void associarPaciente(Long profissionalId, Long pacienteId) {
        Profissional profissional = repository.findById(profissionalId)
                .orElseThrow(() -> new ErrorResponse.ResourceNotFoundException("Profissional com ID " + profissionalId + " não encontrado."));

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ErrorResponse.ResourceNotFoundException("Paciente com ID " + pacienteId + " não encontrado."));

        profissional.getPacientes().add(paciente);
        repository.save(profissional);
    }
}