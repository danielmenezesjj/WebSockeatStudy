package com.estudochat.ws.services;


import com.auth0.jwk.JwkException;
import com.estudochat.ws.data.User;
import com.estudochat.ws.data.UserRepository;
import com.estudochat.ws.providers.TokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    public String buildAndSaveTicket(String token) throws JwkException {
        if(token == null || token.isBlank()) throw new RuntimeException("missing token");
        String ticket = UUID.randomUUID().toString();
        Map<String, String> user = tokenProvider.decode(token);
        String userId = user.get("id");
        redisTemplate.opsForValue().set(ticket, userId, Duration.ofSeconds(60000L));
        saveUser(user);
        return ticket;
    }

    private void saveUser(Map<String, String> user){
        userRepository.save(new User(user.get("id"), user.get("name"), user.get("picture")));
    }

    public Optional<String> getUserIdByTicket(String ticket){
        return Optional.ofNullable(redisTemplate.opsForValue().getAndDelete(ticket));
    }
}