package org.example.service;

import org.example.DTO.NutricionistaCadastroRequest;
import org.example.DTO.NutricionistaResponse;
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

    public NutricionistaResponse cadastrar(NutricionistaCadastroRequest dados) {

        // Validação usa o dado do DTO (CrnUf com 'C' maiúsculo, como no seu DTO)
        if (repository.findByCrnUf(dados.CrnUf()).isPresent()) {
            throw new DuplicateResourceException("CRN já cadastrado!");
        }

        // Mapeia manualmente do DTO para a Entidade
        Nutricionista novaNutricionista = new Nutricionista();
        novaNutricionista.setNome(dados.nome());
        novaNutricionista.setCrnUf(dados.CrnUf());
        novaNutricionista.setEmail(dados.email());

        // Criptografa a senha
        String senhaCriptografada = passwordEncoder.encode(dados.senha());
        novaNutricionista.setSenha(senhaCriptografada);

        // Salva a entidade
        Nutricionista nutricionistaSalva = repository.save(novaNutricionista);

        // Retorna o DTO de Resposta (criado a partir da entidade salva)
        return new NutricionistaResponse(nutricionistaSalva);
    }
}