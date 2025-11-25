package org.example.service;

import org.example.DTO.LoginRequest;
import org.example.DTO.LoginResponse;
import org.example.model.Profissional;
import org.example.model.Paciente;
import org.example.repository.ProfissionalRepository;
import org.example.repository.PacienteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final PacienteRepository pacienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(PacienteRepository pacienteRepository,
                       ProfissionalRepository profissionalRepository,
                       PasswordEncoder passwordEncoder) {
        this.pacienteRepository = pacienteRepository;
        this.profissionalRepository = profissionalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true) // Importante para garantir a sessão do Hibernate
    public Optional<LoginResponse> autenticar(LoginRequest request) {
        System.out.println("--- TENTATIVA DE LOGIN ---");
        System.out.println("Login recebido: " + request.login());

        // 1. Tenta buscar como PROFISSIONAL
        Optional<Profissional> profOpt = this.profissionalRepository.findByRegistroProfissional(request.login());

        if (profOpt.isPresent()) {
            Profissional profissional = profOpt.get();
            System.out.println("Profissional encontrado no Banco: " + profissional.getNome());
            System.out.println("Senha no Banco (Hash): " + profissional.getSenha());
            System.out.println("Senha enviada: " + request.senha());

            boolean senhaBate = passwordEncoder.matches(request.senha(), profissional.getSenha());
            System.out.println("Senha confere? " + senhaBate);

            if (senhaBate) {
                String tipo = profissional.getTipoUsuario();
                System.out.println("Login Sucesso! Tipo: " + tipo);
                return Optional.of(new LoginResponse(profissional.getRegistroProfissional(), tipo));
            } else {
                System.out.println("Falha: Senha incorreta para Profissional.");
            }
        } else {
            System.out.println("Profissional não encontrado com o registro: " + request.login());
        }

        // 2. Tenta buscar como PACIENTE
        Optional<Paciente> pacienteOpt = this.pacienteRepository.findByCpf(request.login());
        if (pacienteOpt.isPresent()) {
            System.out.println("Paciente encontrado: " + pacienteOpt.get().getNome());
            if (passwordEncoder.matches(request.senha(), pacienteOpt.get().getSenha())) {
                return Optional.of(new LoginResponse(pacienteOpt.get().getCpf(), "PACIENTE"));
            } else {
                System.out.println("Falha: Senha incorreta para Paciente.");
            }
        }

        System.out.println("--- FIM DA TENTATIVA (Não autenticado) ---");
        return Optional.empty();
    }
}
