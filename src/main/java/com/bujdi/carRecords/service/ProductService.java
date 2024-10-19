package com.bujdi.carRecords.service;

import com.bujdi.carRecords.model.Product;
import com.bujdi.carRecords.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository repo;

    public List<Product> getProducts() {
        return repo.findAll();
    }

    public Product addProduct(Product prod) {
        return repo.save(prod);
    }

    public Product updateProduct(int productId, Product prod) {
        prod.setId(productId);
        return repo.save(prod);
    }

    public boolean exists(int id) {
        return repo.existsById(id);
    }
}
