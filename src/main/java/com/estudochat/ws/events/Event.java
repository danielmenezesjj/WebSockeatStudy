package com.estudochat.ws.events;



public record Event<T>(EventType type, T payload) {
}
