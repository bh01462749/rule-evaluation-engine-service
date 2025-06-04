package com.hack.rule_evaluation_engine_service.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Data
@Getter
@Setter
public class ChatRequest {
    private List<ChatMessage> messages;

    public ChatRequest() {}
    public ChatRequest(List<ChatMessage> messages) {
        this.messages = messages;
    }
}