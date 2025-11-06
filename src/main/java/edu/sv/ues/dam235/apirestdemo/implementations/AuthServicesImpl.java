package edu.sv.ues.dam235.apirestdemo.implementations;

import edu.sv.ues.dam235.apirestdemo.configs.CustomerDetailServices;
import edu.sv.ues.dam235.apirestdemo.dtos.RegisterDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.TokenDTO;
import edu.sv.ues.dam235.apirestdemo.entities.User;
import edu.sv.ues.dam235.apirestdemo.repositories.UserRepository;
import edu.sv.ues.dam235.apirestdemo.services.AuthServices;
import edu.sv.ues.dam235.apirestdemo.utilities.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServicesImpl implements AuthServices {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomerDetailServices customerDetailServices;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public TokenDTO login(String user, String pass) {
        TokenDTO token = new TokenDTO();
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(user, pass)
                    );
            if (authentication.isAuthenticated()) {
                UserDetails usuarioDetail = (UserDetails)
                        authentication.getPrincipal();
                if (customerDetailServices.getUserDetail().getActive()) {
                    token = jwtUtil.generateToken(user, usuarioDetail);
                    return token;
                }
            }
        } catch (BadCredentialsException bad) {
            token.setMsj("Credenciales incorrectas!");
            return null;
        } catch (Exception e) {
            log.error("{}", e);
            token.setMsj("Error innesperado");
            return null;
        }
        return null;
    }

    @Override
    public boolean register(RegisterDTO userDTO) {
        // Verificar si el usuario ya existe por email
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            return false; // Usuario ya existe
        }
        // Crear y guardar el nuevo usuario
        User newUser = new User();
        newUser.setName(userDTO.getName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());

        // Hashear la contraseña antes de guardarla
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setActive(true); // Activar por defecto

        userRepository.save(newUser);
        return true;
    }
}
