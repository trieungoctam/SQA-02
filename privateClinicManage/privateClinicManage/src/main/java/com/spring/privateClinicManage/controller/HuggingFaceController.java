package com.spring.privateClinicManage.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.spring.privateClinicManage.custom.response.HFResponse.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/hugging-face")
public class HuggingFaceController {

    @Autowired
    private Environment env;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Gson gson;

    public static String hf_token;
    public static String hf_url;

    @PostConstruct
    private void initUrl() {
        hf_token = env.getProperty("HF_TOKEN");
        hf_url = env.getProperty("HF_URL");
    }

    @PostMapping(value = "/completion")
    @CrossOrigin
    public Object chatCompletion(@RequestBody PromptDto promptDto) {

        String prompt = promptDto.getPrompt();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(hf_token);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "Qwen/Qwen2.5-1.5B-Instruct");
        requestBody.put("max_tokens", 512);

        List<MessageDto> messages = new ArrayList<>();
        MessageDto messageDto = new MessageDto();
        List<ContentDto> contentDtoList = new ArrayList<>();
        ContentDto contentDto = new ContentDto();
        contentDto.setText(prompt);
        contentDto.setType("text");
        contentDtoList.add(contentDto);
        messageDto.setContent(contentDtoList);
        messageDto.setRole("user");
        messages.add(messageDto);

        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<HFChatCompletionResponse> response = restTemplate.exchange(
                    hf_url + "/models/Qwen/Qwen2.5-1.5B-Instruct/v1/chat/completions",
                    HttpMethod.POST,
                    entity,
                    HFChatCompletionResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/completion-stream")
    @CrossOrigin
    public Object chatCompletionStream(@RequestParam Map<String, String> params)
            throws URISyntaxException, IOException, InterruptedException {

        String prompt = params.get("prompt");

        SseEmitter sseEmitter = new SseEmitter();

        HttpClient client = HttpClient.newHttpClient();

        String requestBody = String.format(
                """
                        {
                            "model": "google/gemma-2-2b-it",
                            "messages": [
                            {
                                      	"role": "user",
                                      	"content": [
                                      	{
                                      		"type": "text",
                                      		"text": "%s"
                                      	}
                                      			]
                            }
                                      	],
                            "stream": true,
                            "max_tokens": 500
                        }
                        """, prompt);


        HttpRequest requestBuilder = HttpRequest.newBuilder()
//                .uri(new URI(hf_url + "/models/google/gemma-2-2b-it/v1/chat/completions"))
                .uri(new URI(hf_url + "/models/google/gemma-2-2b-it/v1/chat/completions"))
                .header("Authorization", "Bearer " +  hf_token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<InputStream> response = null;
        try {
             response = client.send(requestBuilder,
                    HttpResponse.BodyHandlers.ofInputStream());
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()))) {
            String line;
            ObjectMapper objectMapper = new ObjectMapper();

            while ((line = reader.readLine()) != null) {
                if(!line.trim().isEmpty() && !line.trim().contains("DONE")) {
                    String jsonString = line.substring(6);

                    try {
                        JsonNode rootNode = objectMapper.readTree(jsonString);
                        JsonNode choicesNode = rootNode.path("choices");
                        if (choicesNode.isArray() && !choicesNode.isEmpty()) {
                            String chunk = rootNode
                                    .path("choices")
                                    .get(0)
                                    .path("delta")
                                    .path("content")
                                    .asText();

                            String json = gson.toJson(chunk);

                            try {
                                sseEmitter.send(SseEmitter.event().data(json + "\n\n"));
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        sseEmitter.completeWithError(new Throwable(e.getMessage()));

                    }
                } else if (line.trim().contains("DONE")) {
                    sseEmitter.complete();
                } else if (line.trim().contains("ERROR")) {
                    sseEmitter.complete();
                    System.out.println("ERROR");
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sseEmitter.completeWithError(new Throwable(e.getMessage()));
        }

        return sseEmitter;
    }
}
