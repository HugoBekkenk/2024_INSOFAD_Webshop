package com.example.gamewebshop.dao;

import com.example.gamewebshop.models.CustomUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class UserDAO {

    private final UserRepository userRepository;

    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public CustomUser getUserByEmail(String email){
        Optional<CustomUser> customUser = userRepository.findByEmail(email);  //Custom User is anders bij Eunice dus dit is een tijdelijke oplossing
        return customUser.orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No user found with that email"
        ));
    }

    public void delete(CustomUser existingUser) {
        userRepository.delete(existingUser);
    }

    public CustomUser save(CustomUser existingUser) {
        return userRepository.save(existingUser);
    }
}
