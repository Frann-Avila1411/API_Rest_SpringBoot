package edu.sv.ues.dam235.apirestdemo.implementations;

import edu.sv.ues.dam235.apirestdemo.dtos.ProductsDTO;
import edu.sv.ues.dam235.apirestdemo.entities.Product;
import edu.sv.ues.dam235.apirestdemo.repositories.ProductRepository;
import edu.sv.ues.dam235.apirestdemo.services.ProductServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class ProductsImpl implements ProductServices {
    private final ProductRepository productRepository;

    private ProductsImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductsDTO> getAllProducts() {
        List<ProductsDTO> result = new ArrayList<>();
        List<Product> items = this.productRepository.findAll();
        for (Product item : items) {
            result.add(new ProductsDTO(item.getCode(), item.getName(),
                    item.isStatus()));
        }
        return result;
    }

    @Override
    public ProductsDTO createProduct(ProductsDTO productDTO) {
        Product newProduct = new Product();
        // Asignar propiedades del DTO a la Entidad
        newProduct.setName(productDTO.getName());
        newProduct.setStatus(productDTO.isStatus());

        Product savedProduct = productRepository.save(newProduct);

        // Devolver el DTO con el código generado
        return new ProductsDTO(savedProduct.getCode(), savedProduct.getName(), savedProduct.isStatus());
    }

    @Override
    public ProductsDTO updateProduct(ProductsDTO productDTO) {
        // Buscar el producto por código
        return productRepository.findById(productDTO.getCode())
                .map(product -> {
                    // Asignar nuevas propiedades
                    product.setName(productDTO.getName());
                    product.setStatus(productDTO.isStatus());
                    Product updated = productRepository.save(product);
                    return new ProductsDTO(updated.getCode(), updated.getName(), updated.isStatus());
                })
                .orElse(null); // Retornar null si no se encuentra
    }

    @Override
    public boolean deleteProduct(Integer code) {
        if (productRepository.existsById(code)) {
            productRepository.deleteById(code);
            return true;
        }
        return false;
    }
}
