package com.spring.privateClinicManage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.service.UserService;
import io.github.flyinox.coze4j.CozeClient;
import io.github.flyinox.coze4j.chat.ChatRequest;
import io.github.flyinox.coze4j.conversation.message.ContentType;
import io.github.flyinox.coze4j.conversation.message.MessageRole;
import io.github.flyinox.coze4j.conversation.message.MessageType;
import io.github.flyinox.coze4j.conversation.model.EnterMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@RestController
public class CozeController {

    @Autowired
    private CozeClient cozeClient;
    @Autowired
    private Environment env;
    @Autowired
    private Gson gson;

    @GetMapping(value = "/api/v1/coze/chat-stream")
    @CrossOrigin
    public Object streamChat(@RequestParam Map<String, String> params,
                             HttpServletRequest request, HttpServletResponse response) throws IOException {

        SseEmitter sseEmitter = new SseEmitter();

        response.setHeader("Content-Type", "text/event-stream");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.flushBuffer();

        try {
            String botId = env.getProperty("COZE_BOT_ID");

            List<EnterMessage> additionalMessages = new ArrayList<>();
            EnterMessage enterMessage = new EnterMessage.Builder(MessageRole.USER)
                    .content(params.get("prompt"))
                    .contentType(ContentType.TEXT)
                    .build();
            additionalMessages.add(enterMessage);



            cozeClient.chatStream(new ChatRequest.Builder(botId, UUID.randomUUID().toString()).stream(true)
                    .additionalMessages(additionalMessages).build(), new CozeClient.StreamCallback() {

                @Override
                public void onData(String chunk) {

                    String json = gson.toJson(chunk);

                    try {
                        sseEmitter.send(SseEmitter.event().data(json + "\n\n"));
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onError(Exception e) {
                    sseEmitter.completeWithError(e);
                }

                @Override
                public void onComplete() {
                    sseEmitter.complete();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sseEmitter;
    }

}
