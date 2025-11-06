package edu.sv.ues.dam235.apirestdemo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

     //Bean para configurar CORS de forma global en la aplicación.
     //Esto evita tener que configurar headers en cada respuesta o en el filtro.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite peticiones de cualquier origen
        configuration.setAllowedOrigins(List.of("*"));
        // Permite los métodos HTTP más comunes.
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Permite las cabeceras necesarias para la autenticación y el tipo de contenido.
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Aplica la configuración global de CORS definida arriba.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Deshabilita CSRF, ya que no es necesario para APIs stateless con JWT.
                .csrf(csrf -> csrf.disable())

                // Define las reglas de autorización para las peticiones HTTP.
                .authorizeHttpRequests(auth -> auth
                        // Permite el acceso sin autenticación a todas las rutas bajo /auth/
                        // y a las rutas de Swagger para la documentación.
                        .requestMatchers(
                                "/auth/**",          // Cubre /auth/login, /auth/logout, etc.
                                "/swagger-ui/**",    // Cubre toda la UI de Swagger.
                                "/v3/api-docs/**"    // Cubre la definición de la API para Swagger.
                        )
                        .permitAll()

                        //  Para cualquier otra petición, requiere que el usuario esté autenticado.
                        .anyRequest().authenticated()
                );

        //JwtFilter personalizado antes del filtro estándar de Spring Security.
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

