package org.example.controller;

import org.example.DTO.ChatRequestDTO;
import org.example.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/{pacienteId}")
    public ResponseEntity<Map<String, String>> postChatMessage(
            @PathVariable Long pacienteId,
            @RequestBody ChatRequestDTO request) {

        try {
            String responseMessage = chatService.handleUserMessage(pacienteId, request.getMessage());
            return ResponseEntity.ok(Map.of("response", responseMessage));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("response", "Erro ao processar a requisição: " + e.getMessage()));
        }
    }
}
