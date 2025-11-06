package edu.sv.ues.dam235.apirestdemo.services;

import edu.sv.ues.dam235.apirestdemo.dtos.ProductsDTO;
import java.util.List;

public interface ProductServices {
    public List<ProductsDTO> getAllProducts();
    //metodos crud
    public ProductsDTO createProduct(ProductsDTO productDTO); // crear
    public ProductsDTO updateProduct(ProductsDTO productDTO); // update
    public boolean deleteProduct(Integer code); //borrar
}
