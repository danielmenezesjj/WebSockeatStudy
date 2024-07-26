package com.estudochat.ws.data;


import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;

@Document(collection = "user")
public record User(String id, String name, String picture) {
}

