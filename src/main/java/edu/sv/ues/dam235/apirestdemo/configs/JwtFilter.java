package edu.sv.ues.dam235.apirestdemo.configs;

import edu.sv.ues.dam235.apirestdemo.utilities.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    Claims claims = null;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "false");
        response.setHeader("Access-Control-Max-Age", "3600");
        System.out.println("********** CORS Configuration Completed **********");
// Verificar si es una solicitud preflight (OPTIONS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
// Responder con HTTP 200 y no continuar con el filtro
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        String path = request.getServletPath();
        if (path.startsWith("/auth/login")
                || path.startsWith("/auth/verify-token")
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null &&
                authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        } else {
            token = authorizationHeader;
        }
        if (jwtUtil.validatedTokenPermission(token)) {
            filterChain.doFilter(request, response);
        } else {
// Token no válido o no proporcionado: enviar error 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No autorizado: Token no es el correcto o no proporcionado");
        }
    }

    public Boolean isAdmin() {
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }
    public Boolean isOther() {
        return "user".equalsIgnoreCase((String) claims.get("user"));
    }
}

