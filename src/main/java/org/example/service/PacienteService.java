package org.example.service;

import org.example.DTO.PacienteCadastroRequest;
import org.example.DTO.PacienteResponse;
import org.example.exception.DuplicateResourceException;
import org.example.model.Paciente;
import org.example.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class PacienteService {
    private final PacienteRepository repository;
    private final PasswordEncoder passwordEncoder;

    public PacienteService(PacienteRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public PacienteResponse cadastrar(PacienteCadastroRequest dados) {
        // Verifica se CPF já existe
        if (repository.findByCpf(dados.cpf()).isPresent()) {
            // Lança a exceção específica
            throw new DuplicateResourceException("CPF já cadastrado!");
        }

        // Cria e salva o novo paciente
        Paciente novoPaciente = new Paciente();
        novoPaciente.setNome(dados.nome());
        novoPaciente.setCpf(dados.cpf());
        novoPaciente.setEmail(dados.email());
        novoPaciente.setSenha(passwordEncoder.encode(dados.senha()));

        Paciente pacienteSalvo = repository.save(novoPaciente);
        return new PacienteResponse(pacienteSalvo);
    }
}