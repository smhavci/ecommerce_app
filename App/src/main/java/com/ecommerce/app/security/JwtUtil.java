package com.ecommerce.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ecommerce.app.model.User;
import com.ecommerce.app.repository.UserRepository;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    private final UserRepository userRepository;

    // Token imzalama için sabit KEY (Base64)
    private static final String SECRET_KEY = "6E621262556A586E3272357538782F413F4428472B4B6250655368566D597133";

    // Token süresi (örnek: 24 saat)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // 🔐 Token üretme
    public String generateToken(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole()); // 🔥 Role bilgisi ekleniyor
    
        return buildToken(extraClaims, userDetails);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // ➝ Kullanıcı adı
                .setIssuedAt(new Date(System.currentTimeMillis())) // ➝ Ne zaman üretildi
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // ➝ Ne zaman geçersiz
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 👤 Token'dan kullanıcı adını çek
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ✅ Token geçerli mi?
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // ⏳ Token süresi doldu mu?
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 🧠 Token’dan istediğimiz bilgiyi çekmek
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 🔍 Token çözümleme
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 🔐 Anahtar üretimi (Base64 encoded key)
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
