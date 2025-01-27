package com.utez.integradora.config;


import com.utez.integradora.service.JwtUtils;
import com.utez.integradora.service.UsrDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component("jwtAuth")
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UsrDetailsService usrDetailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, UsrDetailsService usrDetailsService) {
        this.jwtUtils = jwtUtils;
        this.usrDetailsService = usrDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); // Obtener el token de la cabecera
        final String jwtToken;
        final String userEmail;
        if (authHeader == null || authHeader.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = authHeader.substring(7);
        userEmail = jwtUtils.extractUsername(jwtToken); //Extraer el nombre del token

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //Si dentro del contexto de SpringSecurity no existe este usuario autenticado
            UserDetails userDetails = usrDetailsService.loadUserByUsername(userEmail);
            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);

            }
        }
        filterChain.doFilter(request, response);

    }
}
