package com.estudochat.ws.providers;
import com.auth0.jwk.InvalidPublicKeyException;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.UrlJwkProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;

@Component
public class JsonWebKeyProvider implements KeyProvider{


    private final UrlJwkProvider provider;

    public JsonWebKeyProvider(@Value("${app.auth.jwks-url}") final String jwksUrl) throws MalformedURLException {
        try{
            this.provider = new UrlJwkProvider(new URL(jwksUrl));
        }catch (MailAuthenticationException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Cacheable("public-key")
    @Override
    public PublicKey getPublicKey(String keyId) throws JwkException {
        try{
            final Jwk jwk = provider.get(keyId);
            return jwk.getPublicKey();
        }catch (InvalidPublicKeyException e){
            e.printStackTrace();
            throw new RuntimeException();
        }

    }
}
