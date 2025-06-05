package com.hack.rule_evaluation_engine_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.hack.rule_evaluation_engine_service.model.ChatMessage;
import com.hack.rule_evaluation_engine_service.model.ChatRequest;

import java.util.*;


@Slf4j
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

    @Value("${azure.openapi.search.apikey}")
    private String searchApiKey;

    @Value("${azure.openapi.indexName}")
    private String indexName;

    @Value("${azure.openapi.search.endpoint}")
    private String searchEndpoint;

    private final RestTemplate restTemplate = new RestTemplate();

    public String chat(String prompt) throws JsonProcessingException {
        String url = String.format("%s/openai/deployments/%s/chat/completions?api-version=%s",
                endpoint, deploymentId, apiVersion);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);  // Azure OpenAI uses API key as Bearer token
        headers.set("api-key", apiKey);




        Map<String, Object> requestBody = new HashMap<>();

        // Messages array
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        requestBody.put("messages", messages);

        // Data source configuration
        Map<String, Object> auth = new HashMap<>();
        auth.put("type", "api_key");
        auth.put("key", searchApiKey);


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("endpoint", searchEndpoint);
        parameters.put("index_name", indexName);
        parameters.put("authentication", auth);
        parameters.put("semantic_configuration","rag-9-semantic-configuration");
        parameters.put("query_type","vector_semantic_hybrid");

        Map<String,String> embedding_dependency = new HashMap<>();
        embedding_dependency.put("deployment_name","text-embedding-ada-002");
        embedding_dependency.put("type","deployment_name");

        parameters.put("embedding_dependency",embedding_dependency);

        Map<String, Object> dataSource = new HashMap<>();
        dataSource.put("type", "azure_search");
        dataSource.put("parameters", parameters);

        requestBody.put("data_sources", Collections.singletonList(dataSource));


        log.info(requestBody.toString());

        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());


        return root.path("choices").get(0).path("message").path("content").asText();
    }
}