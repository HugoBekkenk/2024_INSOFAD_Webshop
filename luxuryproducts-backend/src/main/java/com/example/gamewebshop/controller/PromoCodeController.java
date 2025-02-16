package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.UserRepository;
import com.example.gamewebshop.models.PromoCode;
import com.example.gamewebshop.services.PromoCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/promocodes")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    private final AdminController adminController;

    public PromoCodeController(PromoCodeService promoCodeService, AdminController adminController) {
        this.promoCodeService = promoCodeService;
        this.adminController = adminController;
    }

    @GetMapping
    public ResponseEntity<List<PromoCode>> fetchAllPromoCodes() {
        List<PromoCode> promoCodes = promoCodeService.retrieveAllPromoCodes();
        return ResponseEntity.ok(promoCodes);
    }

    @PostMapping
    public ResponseEntity<PromoCode> createNewPromoCode(@RequestBody PromoCode promoCode, Principal principal) {
        if (adminController.validateIfAdmin(principal)) {
            PromoCode newPromoCode = promoCodeService.createPromoCode(promoCode);
            return ResponseEntity.ok(newPromoCode);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromoCode> modifyExistingPromoCode(@PathVariable Long id, @RequestBody PromoCode promoCodeDetails, Principal principal) {
        if (adminController.validateIfAdmin(principal)) {
            PromoCode updatedPromoCode = promoCodeService.modifyPromoCode(id, promoCodeDetails);
            return ResponseEntity.ok(updatedPromoCode);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removePromoCode(@PathVariable Long id, Principal principal) {
        if (adminController.validateIfAdmin(principal)) {
            if (!promoCodeService.checkPromoCodeExistsById(id)) {
                return ResponseEntity.notFound().build();
            }
            promoCodeService.removePromoCode(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @GetMapping("/validate")
    public ResponseEntity<?> checkPromoCodeValidity(@RequestParam String code) {
        Optional<PromoCode> promoCode = promoCodeService.fetchPromoCodeByCode(code);
        if (promoCode.isPresent() && promoCodeService.validatePromoCode(code)) {
            return ResponseEntity.ok(Map.of("discount", promoCode.get().getDiscount(), "type", promoCode.get().getType().toString()));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromoCode> fetchPromoCodeById(@PathVariable Long id) {
        Optional<PromoCode> promoCode = promoCodeService.fetchPromoCodeById(id);
        if (promoCode.isPresent()) {
            return ResponseEntity.ok(promoCode.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
