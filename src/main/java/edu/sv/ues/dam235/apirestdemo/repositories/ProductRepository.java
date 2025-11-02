package edu.sv.ues.dam235.apirestdemo.repositories;

import edu.sv.ues.dam235.apirestdemo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,
        Integer> {
}
