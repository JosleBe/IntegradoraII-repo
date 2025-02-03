package com.utez.integradora.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
//permite inyectarlo en otras partes de la aplicacion
public class JwtUtils {
    /*
    GENERACION, VALIDACION Y EXTRACCIÓN DE TOKENS
     */
    private SecretKey secretKey;
    private static final long EXPIRATION_TIME = 60 * 60 * 24 * 7;
    public JwtUtils() {
        //CONIFIGURAR LA CLAVE SECRETA QUE SE USARA PARA FIRMAR LOS TOKENS 0
        String secreteString  = "EstaEsUnaClaveMuySeguraYConSuficientesCaracteresParaHMACSHA256";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.secretKey = new SecretKeySpec(keyBytes, "hmacSHA256");
    }

        public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
            .subject(userDetails.getUsername()) //IDENTIFICADOR (NOMBRE DEL USUARIO)
            .issuedAt(new Date(System.currentTimeMillis())) //CREACION DEL TOKEN
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME )) //EXPIRACION
            .signWith(secretKey) //FIRMA CON LA CLAVE SECRETA
            .compact();
    }

    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }
    //EXTRAER EL NOMBRE DEL USUARIO DEL TOKEN
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }
    //PROCESAR TOKEN PARA EXTRAER UNA PARTE ESPECIFICA DE SUS RECLAMOS
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getBody());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date()); //COMPARACION DE HORAS
    }

}
