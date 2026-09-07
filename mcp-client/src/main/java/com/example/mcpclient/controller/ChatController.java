package com.example.mcpclient.controller;

import com.example.mcpclient.dto.ChatRequest;
import com.example.mcpclient.dto.ChatResponse;
import com.example.mcpclient.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        String answer = chatService.chat(request.message());
        return new ChatResponse(answer);
    }
}