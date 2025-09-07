package com.springsecurity.UtilityPackage;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SimpleOauth2TokenExample {

    // Generate a secret key (in real apps , store this securely)

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long ACCESS_TOKEN_EXPIRY = 15 * 60 * 1000; // 15 mins
    private static final long REFRESH_TOKEN_EXPIRY = 7 * 24 * 60 * 60 * 1000; // 7 days


    // In-memory store for refresh token IDs (jti)
    private static final Set<String> usedRefreshTokens = new HashSet<>();


    // Generate JWT token

    public static String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setIssuer("auth-server")
                .setAudience("your-resource-server")
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000))
                .claim("scope","read write")
                .signWith(SECRET_KEY)
                .compact();
    }

    // Validate JWT token

    public static boolean validateToken(String token,String expectedAudience){
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY)
                    .build().parseClaimsJws(token);

            String audience = claims.getBody().getAudience().toString();
            Date expiration = claims.getBody().getExpiration();

            return audience.equals(expectedAudience) && expiration.before(new Date());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    // For generating Refresh Access Token

    public static String generateRefreshToken(String username){
        String jti = UUID.randomUUID().toString();
        return Jwts.builder().setSubject(username)
                .setId(jti)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ REFRESH_TOKEN_EXPIRY))
                .claim("type","refresh")
                .signWith(SECRET_KEY)
                .compact();
    }

    // to Validate Refresh Token

    public static boolean isRefreshTokenValid(String token){
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build().parseClaimsJws(token);

            String jti = claims.getBody().getId();
            Date expiration = claims.getBody().getExpiration();
            if(usedRefreshTokens.contains(jti)){
                System.out.println("Replay attack detected: Token already used");
                return false;
            }
            if(expiration.before(new Date())){
                System.out.println("Token expired");
                return false;
            }

            // Mark token as used
            usedRefreshTokens.add(jti);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        String username = "ranjan";

        // Initial login
        String accessToken = generateToken(username);
        String refreshToken = generateRefreshToken(username);
        System.out.println("Access Token:\n" + accessToken);
        System.out.println("Refresh Token:\n" + refreshToken);

        // Simulate token refresh
        if (isRefreshTokenValid(refreshToken)) {
            String newAccessToken = generateToken(username);
            String newRefreshToken = generateRefreshToken(username);
            System.out.println("\nNew Access Token:\n" + newAccessToken);
            System.out.println("New Refresh Token:\n" + newRefreshToken);
        }

        // Reuse old refresh token (should fail)
        boolean reused = isRefreshTokenValid(refreshToken);
        System.out.println("\nReusing old refresh token: " + reused);

    }

}
