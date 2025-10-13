package org.example.service;

import org.example.model.Nutricionista;
import org.example.repository.NutricionistaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class NutricionistaService {

    private final NutricionistaRepository repository;
    private final PasswordEncoder passwordEncoder; // 1. Injeta o codificador de senhas

    // 2. Cconstrutor recebe o PasswordEncoder
    public NutricionistaService(NutricionistaRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Nutricionista cadastrar(Nutricionista nutricionista) {
        // Validação para evitar usuários duplicados
        if (repository.findByCrnUf(nutricionista.getCrnUf()).isPresent()) {
            throw new IllegalArgumentException("CRN já cadastrado!");
        }


        // 3. CRIPTOGRAFA a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(nutricionista.getSenha());
        nutricionista.setSenha(senhaCriptografada);

        // 4. Salva o nutricionista com a senha já criptografada
        return repository.save(nutricionista);
    }

}
