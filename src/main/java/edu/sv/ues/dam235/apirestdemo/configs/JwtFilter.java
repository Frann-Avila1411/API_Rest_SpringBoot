package edu.sv.ues.dam235.apirestdemo.configs;

import edu.sv.ues.dam235.apirestdemo.services.TokenBlacklistService;
import edu.sv.ues.dam235.apirestdemo.utilities.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

import java.io.IOException;

@Component // El filtro es un Componente, no una Configuración.
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService; // Inyecta el servicio de la lista negra

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        } else {
            filterChain.doFilter(request, response);
            return;
        }

        // --- LÓGICA DE VALIDACIÓN CENTRAL ---

        //  Comprobar si el token está en la lista negra
        if (tokenBlacklistService.isTokenBlacklisted(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token invalidado por logout.");
            return;
        }

        //  Validar el token usando tu JwtUtil
        if (jwtUtil.validatedTokenPermission(token)) {
            // Si el token es válido y no está en la lista negra, permite que la petición continúe.
            filterChain.doFilter(request, response);
        } else {
            // Si el token no es válido por cualquier otra razón (firma, expiración)...
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No autorizado: Token no es el correcto o ha expirado.");
        }
    }
}
