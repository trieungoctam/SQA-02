package com.spring.privateClinicManage.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class OpenRouterService {
    private final String apiKey;
    private final String model;
    private final RestTemplate restTemplate;
    private final Gson gson;

    public OpenRouterService(String apiKey) {
        this.apiKey = apiKey;
        this.model = "deepseek/deepseek-chat-v3-0324:free";
        this.restTemplate = new RestTemplate();
        this.gson = new Gson();
    }

    public String generateText(String prompt) {
        // Create message object
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(userMessage);
        
        // Create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", messages);
        
        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        
        // Create HTTP entity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        // Make request
        ResponseEntity<String> response = restTemplate.exchange(
            "https://openrouter.ai/api/v1/chat/completions",
            HttpMethod.POST,
            entity,
            String.class
        );
        
        // Parse response
        JsonObject responseJson = JsonParser.parseString(response.getBody()).getAsJsonObject();
        return responseJson
            .getAsJsonArray("choices")
            .get(0)
            .getAsJsonObject()
            .getAsJsonObject("message")
            .get("content")
            .getAsString();
    }
}