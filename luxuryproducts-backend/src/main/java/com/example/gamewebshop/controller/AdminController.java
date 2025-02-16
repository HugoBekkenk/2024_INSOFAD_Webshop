package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.*;
import com.example.gamewebshop.dto.CategoryDTO;
import com.example.gamewebshop.dto.OptionDTO;
import com.example.gamewebshop.dto.ProductDTO;
import com.example.gamewebshop.dto.VariantDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.PlacedOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://s1148232.student.inf-hsleiden.nl:18232"})
@RequestMapping("/admin")
public class AdminController {
    private final ProductDAO productDAO;
    private final UserRepository userRepository;
    private final OrderDAO orderDAO;
    private final CategoryDAO categoryDAO;
    private final VariantDAO variantDAO;
    private final OptionDAO optionDAO;


    public AdminController(ProductDAO productDAO, UserRepository userRepository, OrderDAO orderDAO, CategoryDAO categoryDAO, VariantDAO variantDAO, OptionDAO optionDAO) {
        this.productDAO = productDAO;
        this.userRepository = userRepository;
        this.orderDAO = orderDAO;
        this.categoryDAO = categoryDAO;
        this.variantDAO = variantDAO;
        this.optionDAO = optionDAO;
    }

    public boolean validateIfAdmin(Principal principal){
        String userEmail = principal.getName();
        Optional<CustomUser> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User is not found"
            );
        }else {
            if (Objects.equals(user.get().getAuthority(), "admin")) {
                return true;
            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User is unauthorized for this function"
                );
            }
        }
    }

    @PostMapping("/product")
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO, Principal principal){
        if (validateIfAdmin(principal)){
            this.productDAO.createProduct(productDTO);
            return ResponseEntity.ok("Created a product");
        } else {
            return ResponseEntity.ok("Unauthorized");
        }
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO, Principal principal){
        if (validateIfAdmin(principal)){
            this.productDAO.updateProduct(productDTO, id);
            return ResponseEntity.ok("Updated product with id" + id);
        } else {
            return ResponseEntity.ok("Unauthorized");
        }

    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id, Principal principal){
        if (validateIfAdmin(principal)){
            this.productDAO.deleteProductById(id);
            return ResponseEntity.ok("Product deleted with id " + id);
        } else {
            return ResponseEntity.ok("Unauthorized");
        }
    }

    @PostMapping("/category")
    public ResponseEntity<String> createCategory(@RequestBody CategoryDTO categoryDTO, Principal principal){
        if (validateIfAdmin(principal)){
            this.categoryDAO.createCategory(categoryDTO);
            return ResponseEntity.ok("Created a new category named " + categoryDTO.name);
        } else {
            return ResponseEntity.ok("Unauthorized");
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<List<PlacedOrder>> getAllOrders(Principal principal){
        if (validateIfAdmin(principal)){
            return ResponseEntity.ok(this.orderDAO.getAllOrders());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User is unauthorized for this function"
            );
        }
    }

    @PostMapping("/variant")
    public ResponseEntity<String> createVariant(@RequestBody VariantDTO variantDTO, Principal principal){
        if (validateIfAdmin(principal)){
            this.variantDAO.createVariant(variantDTO);
            return ResponseEntity.ok("Created a variant");
        } else {
            return ResponseEntity.ok("Unauthorized");
        }

    }

    @DeleteMapping("/variant/{id}")
    public ResponseEntity<String> deleteVariant(@PathVariable Long id, Principal principal){
        if (validateIfAdmin(principal)){
            this.variantDAO.deleteVariantById(id);
            return ResponseEntity.ok("variant deleted with id " + id);
        } else {
            return ResponseEntity.ok("Unauthorized");
        }
    }

    @PostMapping("/option")
    public ResponseEntity<String> createOption(@RequestBody OptionDTO optionDTO, Principal principal){
        if (validateIfAdmin(principal)){
            this.optionDAO.createOption(optionDTO);
            return ResponseEntity.ok("Created a option");
        } else {
            return ResponseEntity.ok("Unauthorized");
        }

    }

    @DeleteMapping("/option/{id}")
    public ResponseEntity<String> deleteOption(@PathVariable Long id, Principal principal){
        if (validateIfAdmin(principal)){
            this.optionDAO.deleteById(id);
            return ResponseEntity.ok("option deleted with id " + id);
        } else {
            return ResponseEntity.ok("Unauthorized");
        }
    }


    @PutMapping("/user")
    public ResponseEntity<CustomUser> updateUser(@RequestBody CustomUser updatedUser, Principal principal) {
        if (validateIfAdmin(principal)){
            Optional<CustomUser> optionalExistingUser = userRepository.findByEmail(updatedUser.getName());

            if (optionalExistingUser.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden");
            }
            CustomUser existingUser = optionalExistingUser.get();
            existingUser.setName(updatedUser.getName());
            existingUser.setInfix(updatedUser.getInfix());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());


            CustomUser savedUser = userRepository.save(existingUser);
            return ResponseEntity.ok(savedUser);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User is unauthorized for this function"
            );
        }
    }


    @DeleteMapping("/user/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable("email") String email, Principal principal) {
        if (validateIfAdmin(principal)){
            Optional<CustomUser> existingUser = userRepository.findByEmail(email);
            if (existingUser.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden voor e-mail: " + email);
            }
            userRepository.delete(existingUser.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User is unauthorized for this function"
            );
        }

    }

    @GetMapping("/user")
    public ResponseEntity<List<CustomUser>> getAllUsers(Principal principal){
        if (validateIfAdmin(principal)){
            return ResponseEntity.ok(userRepository.findAll());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User is unauthorized for this function"
            );
        }
    }

}
