package edu.sv.ues.dam235.apirestdemo.dtos;

import lombok.Data;

// DTO para recibir datos del cliente al registrarse
@Data //Para el uso de getters y setters aunque no esten escritos en el codigo
public class RegisterDTO {
    private String name;
    private String lastName;
    private String email;
    private String password;
}
