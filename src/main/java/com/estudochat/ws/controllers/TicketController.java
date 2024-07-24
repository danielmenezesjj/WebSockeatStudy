package com.estudochat.ws.controllers;


import com.auth0.jwk.JwkException;
import com.estudochat.ws.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("v1/ticket")
@CrossOrigin
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public Map<String, String> buildTicket(
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            String authorization
    ) throws JwkException {
        System.out.println("Caiu aqui");
        String token = Optional
                .ofNullable(authorization)
                .map(it -> it.replace("Bearer ", ""))
                .orElse("");
        String ticket = ticketService.buildAndSaveTicket(token);
        return Map.of("ticket", ticket);
    }
}
