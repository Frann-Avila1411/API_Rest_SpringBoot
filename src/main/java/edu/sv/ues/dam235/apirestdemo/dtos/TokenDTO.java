package edu.sv.ues.dam235.apirestdemo.dtos;

import lombok.Data;

@Data
public class TokenDTO {
    private String token;
    private String expireIn;
    private String msj;
}
