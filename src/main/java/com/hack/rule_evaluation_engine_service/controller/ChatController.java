package com.hack.rule_evaluation_engine_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hack.rule_evaluation_engine_service.model.ChatMessage;
import com.hack.rule_evaluation_engine_service.model.ChatRequest;
import com.hack.rule_evaluation_engine_service.service.ChatService;

@Slf4j
@RestController
@RequestMapping("/api/openai")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/ask")
    public ResponseEntity<String> ask(@RequestParam String prompt) {

        try {
            return ResponseEntity.ok(chatService.chat(prompt));
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}