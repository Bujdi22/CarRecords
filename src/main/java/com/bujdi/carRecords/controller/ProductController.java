package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.dto.ProductDto;
import com.bujdi.carRecords.model.Product;
import com.bujdi.carRecords.service.ProductService;
import com.bujdi.carRecords.service.UserService;
import com.bujdi.carRecords.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService service;

    @Autowired
    VendorService vendorService;

    @Autowired
    UserService userService;

    @GetMapping("/api/products")
    public List<Product> getProducts() {
        System.out.println("auth id =" + userService.getAuthUser().getId());
        return service.getProducts();
    }

    @PostMapping("/api/products")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductDto prod)
    {
        Product savedProduct = service.addProduct(this.createProductFromDTO(prod));

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<Product> updateProduct(
        @PathVariable("productId") int productId,
        @Valid @RequestBody ProductDto prod
    ){
        if (service.exists(productId)) {
            Product savedProduct = service.updateProduct(productId, this.createProductFromDTO(prod));

            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private Product createProductFromDTO(ProductDto dto) {
        Product prod = new Product();

        prod.setName(dto.getName());
        prod.setDescription(dto.getDescription());
        prod.setPrice(dto.getPrice());
        prod.setQuantity(dto.getQuantity());
        prod.setAvailable(dto.isAvailable());
        prod.setVendor(this.vendorService.getVendorById(dto.getVendorId()));

        return prod;
    }
}
