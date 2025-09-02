package org.redmath.taskmanagement.controller;

import org.redmath.taskmanagement.dto.ChatReqDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/gemini")
public class AIChatController {

    private final ChatClient chat;

    public AIChatController(ChatClient chat) { this.chat = chat; }

    @PostMapping("/simple-response")
    public ResponseEntity<?> chat(@RequestBody ChatReqDto req) {
        System.out.println("hit");
        String out = chat.prompt()
                .user(req.prompt())
                .call()
                .content();
        return ResponseEntity.ok(java.util.Map.of("response", out));
    }



}
