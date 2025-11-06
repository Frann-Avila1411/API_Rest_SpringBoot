package edu.sv.ues.dam235.apirestdemo.services;

import edu.sv.ues.dam235.apirestdemo.dtos.RegisterDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.TokenDTO;

public interface AuthServices {
    public TokenDTO login(String user, String pass);
    public boolean register(RegisterDTO userDTO);
    // Logout no requiere endpoint en JWT Stateless
}