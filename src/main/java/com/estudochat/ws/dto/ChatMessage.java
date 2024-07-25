package com.estudochat.ws.dto;

import com.estudochat.ws.data.User;

public record ChatMessage(User from, User to, String text) {
}
