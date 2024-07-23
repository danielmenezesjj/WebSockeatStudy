package com.estudochat.ws.providers;

import com.auth0.jwk.JwkException;

import java.security.PublicKey;

public interface KeyProvider {

    PublicKey getPublicKey(String keyId) throws JwkException;

}
