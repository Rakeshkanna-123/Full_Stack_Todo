package com.example.todo.Utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET = "Hello todo iam rakesh kanna from coimbatore nice meeting you";

    private final Key secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    //this line encrypts the secret string into tokens return it as Key(class) value which is used to authenticate the user

    private final long EXPIRATION = 1000 * 60 * 15;//now i kept for 15 minutes
    //1000ms = 1s , 1000ms * 60 = 60000ms = 1minute, so the token validity will end in 1 minute, new key is generated if the user login after 1 minute

    //for example if i give abc@gmail.com it is tokenized as ygf38yvf8qyiv87v8ygq8vf  using the sha encrypting algorithm

    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /*.signWith(secretKey, SignatureAlgorithm.ES256)
    this line is like a seal operation, when we deliver a product to some other place we seal it and send
    if the seal is broken then we come to know that the parcel has been opened by someone and the parcel
    will not be accepted. Like wise this signWith method checks whether the secreKey is exposed or not using SignatureAlgorithm.ES256
     */

    public String extractEmail(String token){
        System.out.println("Email extraction begins");
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean validateJWTToken(String token){

        try{
            extractEmail(token);
            return true;
        }catch(JwtException e){
            return false;
        }
    }
}
