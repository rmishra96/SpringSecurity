package com.springsecurity.Oauth2.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;

@Component
public class OAuthTokenValidatorUtil {

    public String isTokenValid(String accessToken){
        String issue = getIssuerIdFromToken(accessToken);
        JwtDecoder decoder = JwtDecoders.fromIssuerLocation(issue);
        Jwt jwt = decoder.decode(accessToken);
        if(jwt != null){
            return (String) jwt.getClaims().get("sub");
        }
        return null;
    }

    private static String getIssuerIdFromToken(String jwtToken) {
        try {
            String[] parts = jwtToken.split("\\.");
            if(parts.length < 2) {
                throw new IllegalArgumentException("Invalid JWT token.");
            }

            String payLoadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payloadMap = mapper.readValue(payLoadJson,Map.class);
            String iss = payloadMap.get("iss").toString();
            return iss;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);

        }
    }
}
