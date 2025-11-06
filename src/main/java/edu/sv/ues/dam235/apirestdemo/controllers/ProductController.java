package edu.sv.ues.dam235.apirestdemo.controllers;

import edu.sv.ues.dam235.apirestdemo.dtos.ProductsDTO;
import edu.sv.ues.dam235.apirestdemo.services.ProductServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {
    final private ProductServices productServices;
    private ProductController(ProductServices productServices) {
        this.productServices = productServices;
    }
    @GetMapping
    public ResponseEntity<List<ProductsDTO>> getAllItems() {
        try {
            List<ProductsDTO> items = productServices.getAllProducts();
            if (items.isEmpty()) {
                return ResponseEntity.status(204).build();
            } else {
                return ResponseEntity.ok(items);
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }
    //Crear Producto
    @PostMapping //Crear Producto
    public ResponseEntity<ProductsDTO> createItem(@RequestBody ProductsDTO productDTO) {
        ProductsDTO newProduct = productServices.createProduct(productDTO);
        return ResponseEntity.status(201).body(newProduct); // 201 Created
    }

    //Actualizar Producto
    @PutMapping
    public ResponseEntity<ProductsDTO> updateItem(@RequestBody ProductsDTO productDTO) {
        ProductsDTO updatedProduct = productServices.updateProduct(productDTO);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.status(404).build(); // Not Found
        }
    }

    // Eliminar Producto
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer code) {
        if (productServices.deleteProduct(code)) {
            return ResponseEntity.ok().build(); // 200 OK
        } else {
            return ResponseEntity.status(404).build(); // Not Found
        }
    }
}

