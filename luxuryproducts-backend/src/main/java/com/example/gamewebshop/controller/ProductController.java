package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.ProductDAO;
import com.example.gamewebshop.dto.ProductDTO;
import com.example.gamewebshop.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://s1148232.student.inf-hsleiden.nl:18232"})
@RequestMapping("/products")
public class ProductController {

    private final ProductDAO productDAO;

    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(this.productDAO.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){

        return ResponseEntity.ok(this.productDAO.getProductById(id));
    }

    @GetMapping(params = "categoryId")
    public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam Long categoryId){

        return ResponseEntity.ok(this.productDAO.getAllProductsByCategory(categoryId));
    }

}
