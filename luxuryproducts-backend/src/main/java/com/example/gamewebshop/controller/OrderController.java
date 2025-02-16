package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.OrderDAO;
import com.example.gamewebshop.dao.UserRepository;
import com.example.gamewebshop.dto.OrderDTO;
import com.example.gamewebshop.dto.OrderResponseDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.GiftCard;
import com.example.gamewebshop.models.PlacedOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://s1148232.student.inf-hsleiden.nl:18232"})
@RequestMapping("/orders")
public class OrderController {

    private final OrderDAO orderDAO;
    private final UserRepository userRepository;

    public OrderController(OrderDAO orderDAO, UserRepository userRepository) {
        this.orderDAO = orderDAO;
        this.userRepository = userRepository;
    }


    @GetMapping("/myOrders")
    public ResponseEntity<List<PlacedOrder>> getOrdersByUserPrincipal(Principal principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().build();
        }
        String userEmail = principal.getName();
        Optional<CustomUser> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<PlacedOrder> orders = this.orderDAO.getOrdersByUserId(user.get().getId());
        return ResponseEntity.ok(orders);
    }


    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderDTO placedOrder, Principal principal) {
        String userEmail = principal.getName();
        return ResponseEntity.ok().body(orderDAO.saveOrderWithProducts(placedOrder, userEmail));
    }

}
