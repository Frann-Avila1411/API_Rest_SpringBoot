package edu.sv.ues.dam235.apirestdemo.entities;
import jakarta.persistence.*;
import lombok.Data;

@Data //con esto se tienen los getters y setters
@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Code")
    private Integer code;
    @Column(name = "Name")
    private String name;
    @Column(name = "Status")
    private boolean status;
}