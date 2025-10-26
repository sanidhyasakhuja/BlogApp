package com.example.blogApp.security;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTHelper {

public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	private static final String secretKey="m24R3jhMlR8V9Ub9BfprPgf8ZC8fUgnCj3wKlLh7m+M=";
	

	public String generateToken(String username,Integer userId,Set<String> userRole) {
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("userId", userId);
		claims.put("userRole", userRole);
		return Jwts.builder()
		    .claims()
		    .add(claims)
		    .subject(username)
		    .issuedAt(new Date(System.currentTimeMillis()))
		    .expiration(new Date(System.currentTimeMillis()+ JWT_TOKEN_VALIDITY *1000))
		    .and()
		    .signWith(getKey())
		    .compact();
		
	}

	private SecretKey getKey() {
		byte [] keyBytes = Base64.getDecoder().decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
