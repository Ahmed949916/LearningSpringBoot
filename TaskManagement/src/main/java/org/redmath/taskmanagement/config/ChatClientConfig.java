package org.redmath.taskmanagement.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder().maxMessages(20).build();
    }
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder,ChatMemory memory){
        return builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build()).build();
    }


}
