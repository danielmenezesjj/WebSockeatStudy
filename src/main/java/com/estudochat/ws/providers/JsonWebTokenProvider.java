package com.estudochat.ws.providers;

import com.auth0.jwk.JwkException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;


@Component
public class JsonWebTokenProvider implements TokenProvider {

    @Autowired
    private KeyProvider keyProvider;

    @Override
    public Map<String, String> decode(String token) {
        try {
            // Decode the token
            DecodedJWT jwt = JWT.decode(token);

            // Get the public key from the key provider
            PublicKey publicKey = keyProvider.getPublicKey(jwt.getKeyId());

            // Create an RSA256 algorithm instance with the public key
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);

            // Create a JWT verifier
            JWTVerifier verifier = JWT.require(algorithm).build();

            // Verify the token (this will check the signature and the exp claim)
            verifier.verify(jwt);

            // Check if the token is expired
            Instant expiresAt = jwt.getExpiresAtAsInstant();
            Instant now = Instant.now().minusSeconds(30); // Add a 30-second margin
            if (expiresAt.isBefore(now)) {
                throw new RuntimeException("Token is expired");
            }

            // Return the token claims
            return Map.of(
                    "id", jwt.getSubject(),
                    "name", jwt.getClaim("name").asString(),
                    "picture", jwt.getClaim("picture").asString()
            );
        } catch (TokenExpiredException e) {
            // Handle the token expired exception specifically
            throw e;
        } catch (Exception e) {
            // Handle other exceptions accordingly
            throw new RuntimeException("Invalid token", e);
        }
    }
}
