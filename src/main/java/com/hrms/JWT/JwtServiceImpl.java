package com.hrms.JWT;

import com.hrms.Security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService{
    private final String SECRET = "mysupersecretkeymysupersecretkey"; // use at least 256 bits!
    private final long EXPIRATION = 36000 ; // 1 hour


    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(CustomUserDetails customUserDetails) {

        Instant now = Instant.now();
        Instant notBefore = now.plusSeconds(1);

        List<String> roles = customUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return Jwts
                .builder()
                .setSubject(customUserDetails.getUsername())
                .setId(UUID.randomUUID().toString())
                .claim("roles", roles)
                .claim("isEnabled", customUserDetails.isEnabled())
                .setIssuedAt(Date.from(Instant.now()))
                .setNotBefore(Date.from(notBefore))
                .setExpiration(Date.from(now.plusSeconds(EXPIRATION)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256 )
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



    @Override
    public String extractEmail(String token) {
        return "";
    }

    @Override
    public boolean isTokenValid(String token) {
        return false;
    }
}