package edu.sv.ues.dam235.apirestdemo.controllers;

import edu.sv.ues.dam235.apirestdemo.dtos.LoginDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.RegisterDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.TokenDTO;
import edu.sv.ues.dam235.apirestdemo.services.AuthServices;
import edu.sv.ues.dam235.apirestdemo.services.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    final private AuthServices authServices;
    private AuthController(AuthServices authServices) {
        this.authServices = authServices;
    }

    @Autowired
    private TokenBlacklistService tokenBlacklistService;// Endpoint para logout
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        // Obtenemos el header "Authorization"
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extraemos el token (sin el prefijo "Bearer ")
            String token = authHeader.substring(7);
            tokenBlacklistService.addTokenToBlacklist(token);
        }

        return ResponseEntity.ok().build();
    }
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO
                                                  authRequest) {
        try {
            System.out.println("DTO enviado : " +
                    authRequest.toString());
            TokenDTO token = authServices.login(authRequest.getUser(),
                    authRequest.getPass());
            if (token == null) {
                return ResponseEntity.status(401).build();
            } else {
                return ResponseEntity.ok(token);
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }

    // Endpoint de Registro
    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody RegisterDTO registerDTO) {
        try {
            boolean success = authServices.register(registerDTO);
            if (success) {
                // El usuario fue creado exitosamente
                return ResponseEntity.status(201).body(true);
            } else {
                // El usuario ya existe o hubo un problema
                return ResponseEntity.status(409).body(false); // 409 Conflict
            }
        } catch (Exception e) {
            log.error("Error al registrar", e);
            return ResponseEntity.status(500).body(false);
        }
    }
}
