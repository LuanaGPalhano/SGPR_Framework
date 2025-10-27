package org.example.controller;

import org.example.DTO.CardapioFavoritoDTO;
import org.example.DTO.ChatRequestDTO;
import org.example.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat/{pacienteId}")
    public ResponseEntity<Map<String, String>> postChatMessage(
            @PathVariable Long pacienteId,
            @RequestBody ChatRequestDTO request) throws IOException { // Apenas declara IOException

        // Se handleUserMessage lançar uma exceção, o GlobalExceptionHandler vai pegá-la
        // e retornar uma resposta de erro 500 (Internal Server Error) padronizada.
        String responseMessage = chatService.handleUserMessage(pacienteId, request.getMessage());
        return ResponseEntity.ok(Map.of("response", responseMessage));
    }

    @GetMapping("/favoritos/{pacienteId}")
    public ResponseEntity<List<CardapioFavoritoDTO>> getFavoritos(@PathVariable Long pacienteId) {

        // Se buscarFavoritosPorPaciente lançar ResourceNotFoundException (ou outra),
        // o GlobalExceptionHandler vai pegá-la e retornar um 404 (Not Found).
        List<CardapioFavoritoDTO> favoritosDTO = chatService.buscarFavoritosPorPaciente(pacienteId);
        return ResponseEntity.ok(favoritosDTO);
    }

    @DeleteMapping("/favoritos/{favoritoId}/paciente/{pacienteId}")
    public ResponseEntity<Void> deleteFavorito(
            @PathVariable Long favoritoId,
            @PathVariable Long pacienteId) {

        // O ChatService lançará:
        // 1. ResourceNotFoundException (ou NoSuchElementException) -> O Handler pegará e retornará 404.
        // 2. UnauthorizedOperationException (ou SecurityException) -> O Handler pegará e retornará 403.

        chatService.excluirFavorito(favoritoId, pacienteId);
        // Se nenhuma exceção for lançada, retorna 204 No Content (sucesso na exclusão).
        return ResponseEntity.noContent().build();
    }
}