package com.hack.rule_evaluation_engine_service.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChatMessage {
    private String role;
    private String content;

    public ChatMessage() {}
    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}