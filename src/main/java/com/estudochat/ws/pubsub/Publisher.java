package com.estudochat.ws.pubsub;


import com.estudochat.ws.config.RedisConfig;
import com.estudochat.ws.data.User;
import com.estudochat.ws.data.UserRepository;
import com.estudochat.ws.dto.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class Publisher {

    private final static Logger LOGGER = Logger.getLogger(Publisher.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;

    public void publishChatMessage(String userIdFrom, String userIdTo, String text) throws JsonProcessingException {
        User from = userRepository.findById(userIdFrom).orElseThrow();
        User to = userRepository.findById(userIdTo).orElseThrow();
        ChatMessage chatMessage = new ChatMessage(from, to, text);
        String chatMessageSerialized = new ObjectMapper().writeValueAsString(chatMessage);
        redisTemplate
                .convertAndSend(RedisConfig.CHAT_MESSAGES_CHANNEL, chatMessageSerialized)
                .subscribe();
        LOGGER.info("chat message was published");
    }
}
