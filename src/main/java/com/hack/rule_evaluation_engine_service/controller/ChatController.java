package com.hack.rule_evaluation_engine_service.controller;

import org.springframework.web.bind.annotation.*;
import com.hack.rule_evaluation_engine_service.model.ChatMessage;
import com.hack.rule_evaluation_engine_service.model.ChatRequest;
import com.hack.rule_evaluation_engine_service.service.ChatService;

@RestController
@RequestMapping("/api/openai")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String prompt) {
        return chatService.chat(prompt);
    }
}