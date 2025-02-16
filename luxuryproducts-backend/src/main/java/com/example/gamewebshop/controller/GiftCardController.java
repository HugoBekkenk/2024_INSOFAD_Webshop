package com.example.gamewebshop.controller;

import com.example.gamewebshop.dto.ApiResponseDTO;
import com.example.gamewebshop.dao.GiftCardDAO;
import com.example.gamewebshop.dao.UserDAO;
import com.example.gamewebshop.dao.UserGiftCardDAO;
import com.example.gamewebshop.dto.GiftCardDTO;
import com.example.gamewebshop.dto.MiniGiftCardDTO;
import com.example.gamewebshop.dto.UserGiftCardDTO;
import com.example.gamewebshop.models.GiftCard;
import com.example.gamewebshop.models.UserGiftCard;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;


@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://s1148232.student.inf-hsleiden.nl:18232"})
@RequestMapping("/giftCards")
public class GiftCardController {

    private final GiftCardDAO giftCardDAO;
    private final UserGiftCardDAO userGiftCardDAO;
    private final UserDAO userDAO;
    private final AdminController adminController;

    public GiftCardController(GiftCardDAO giftCardDAO,UserGiftCardDAO userGiftCardDAO,UserDAO userDAO, AdminController adminController) {
        this.giftCardDAO = giftCardDAO;
        this.userGiftCardDAO = userGiftCardDAO;
        this.userDAO = userDAO;
        this.adminController = adminController;
    }

    @GetMapping
    public ResponseEntity<List<GiftCard>> getAllActiveGiftCards(){
        return ResponseEntity.ok(this.giftCardDAO.getAllActiveGiftCards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCard> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(this.giftCardDAO.getGiftCardById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> create(@RequestBody GiftCard giftCard, Principal principal){
        if (adminController.validateIfAdmin(principal)){
            this.giftCardDAO.createGiftCard(giftCard);
            return new ResponseEntity<>(new ApiResponseDTO("Gift card has been added",HttpStatus.OK.value()),HttpStatus.OK);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User is unauthorized for this function"
            );
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody GiftCardDTO giftCardDTO, Principal principal){
        if (adminController.validateIfAdmin(principal)){
            this.giftCardDAO.updateGiftCard(giftCardDTO, id);
            return ResponseEntity.ok("Updated Gift Card with id" + id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User is unauthorized for this function"
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteById(@PathVariable Long id, Principal principal){
        if (adminController.validateIfAdmin(principal)){
            this.giftCardDAO.deleteById(id);
            return new ResponseEntity<> (new ApiResponseDTO("GiftCard deleted with id " + id,HttpStatus.OK.value()),HttpStatus.OK);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User is unauthorized for this function"
            );
        }
    }
    @PostMapping("/{email}/{giftCardId}")
    public ResponseEntity<ApiResponseDTO> sendGiftCard(@PathVariable String email, @PathVariable Long giftCardId){
        userGiftCardDAO.saveUserGiftCard(email,giftCardId);
        return new ResponseEntity<>(new ApiResponseDTO("Gift Card sent to user with email : " + email,HttpStatus.OK.value()),HttpStatus.OK);
    }
    @GetMapping("/getAllGiftCardSentByUser")
    public List<UserGiftCard> getAllGiftCardSentByUser(Principal principal){
        return userGiftCardDAO.getAllGiftCardsSentByUser(userDAO.getUserByEmail(principal.getName()).getId());
    }
    @GetMapping("/getAllGiftCardsReceivedByUser")
    public List<UserGiftCard> getAllGiftCardsReceivedByUser(Principal principal){
        return userGiftCardDAO.getAllGiftCardsReceivedByUser(userDAO.getUserByEmail(principal.getName()).getId());
    }
    @GetMapping("/mini")
    public ResponseEntity<List<MiniGiftCardDTO>> getAllActiveGiftCardsFoDropDown(Principal principal){
        return ResponseEntity.ok(this.userGiftCardDAO.getGiftCardsDropDown(userDAO.getUserByEmail(principal.getName()).getId()));
    }
    @GetMapping("/users")
    public List<UserGiftCardDTO> getAllUserGiftCards(Principal principal){
        if (adminController.validateIfAdmin(principal)){
            return userGiftCardDAO.getAllUserGiftCars();
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User is unauthorized for this function"
            );
        }
    }
}
