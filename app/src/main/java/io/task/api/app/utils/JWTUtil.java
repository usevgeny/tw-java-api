package io.task.api.app.utils;

import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTUtil {

    @Value("${application.jwt_secret}")
    private String JWTsecret;

    public String generateToken(String username) {
        
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(AppConstants.EXPIRATION_TIME).toInstant());
        
        return JWT.create()
                .withSubject(AppConstants.JWT_TOKEN_SUBJECT)
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer(AppConstants.TASK_API_APP)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(String.valueOf(JWTsecret)))
                ;
        
    }
    
    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWTsecret))
            .withSubject(AppConstants.JWT_TOKEN_SUBJECT)
            .withIssuer(AppConstants.TASK_API_APP)
            .build();
        
        DecodedJWT jwt = verifier.verify(token);
        
        return jwt.getClaim("username").asString();
    }
    
    

}
