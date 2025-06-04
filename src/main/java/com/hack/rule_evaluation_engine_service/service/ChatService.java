package com.hack.rule_evaluation_engine_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.hack.rule_evaluation_engine_service.model.ChatMessage;
import com.hack.rule_evaluation_engine_service.model.ChatRequest;
import java.util.List;

@Service
public class ChatService {

    @Value("${azure.openai.endpoint}")
    private String endpoint;

    @Value("${azure.openai.api-key}")
    private String apiKey;

    @Value("${azure.openai.deployment-id}")
    private String deploymentId;

    @Value("${azure.openai.api-version}")
    private String apiVersion;

    private final RestTemplate restTemplate = new RestTemplate();

    public String chat(String prompt) throws JsonProcessingException {
        String url = String.format("%s/openai/deployments/%s/chat/completions?api-version=%s",
                endpoint, deploymentId, apiVersion);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);  // Azure OpenAI uses API key as Bearer token
        headers.set("api-key", apiKey);

        ChatRequest body = new ChatRequest(List.of(new ChatMessage("user", prompt)));

        HttpEntity<ChatRequest> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());


        return root.path("choices").get(0).path("message").path("content").asText();
    }
}