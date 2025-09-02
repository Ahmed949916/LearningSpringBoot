package org.redmath.taskmanagement.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/gemini")
public class AIChatController {

    private final ChatClient chat;

    public AIChatController(ChatClient chat) { this.chat = chat; }

    @PostMapping("/simple-response")
    public java.util.Map<String, Object> chat(@RequestBody org.redmath.taskmanagement.dto.ChatReqDto req) {
        String out = chat.prompt().user(req.prompt()).call().content();
        return java.util.Map.of("response", out);
    }


    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> stream(@RequestParam("prompt") String prompt) {

        return chat.prompt()
                .user(prompt)
                .stream()
                .content()
                .map(tok -> ServerSentEvent.builder(tok).build());
    }
}
