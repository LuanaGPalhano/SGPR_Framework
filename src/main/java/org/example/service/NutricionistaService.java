package org.example.service;

import org.example.exception.DuplicateResourceException;
import org.example.model.Nutricionista;
import org.example.repository.NutricionistaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class NutricionistaService {

    private final NutricionistaRepository repository;
    private final PasswordEncoder passwordEncoder;

    public NutricionistaService(NutricionistaRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Nutricionista cadastrar(Nutricionista nutricionista) {
        // Verifica se CRN já existe
        if (repository.findByCrnUf(nutricionista.getCrnUf()).isPresent()) {
            // Lança a exceção específica
            throw new DuplicateResourceException("CRN já cadastrado!");
        }

        // Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(nutricionista.getSenha());
        nutricionista.setSenha(senhaCriptografada);

        return repository.save(nutricionista);
    }
}