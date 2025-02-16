package com.example.gamewebshop.controller;

import com.example.gamewebshop.config.JWTUtil;
import com.example.gamewebshop.dao.UserRepository;
import com.example.gamewebshop.dto.AuthenticationDTO;
import com.example.gamewebshop.dto.LoginResponse;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.services.CredentialValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://s1148232.student.inf-hsleiden.nl:18232"})
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userDAO;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private CredentialValidator validator;

    public AuthController(UserRepository userDAO, JWTUtil jwtUtil, AuthenticationManager authManager,
                          PasswordEncoder passwordEncoder, CredentialValidator validator) {
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }



    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody AuthenticationDTO authenticationDTO) {
        if (!validator.isValidEmail(authenticationDTO.email)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No valid email provided"
            );
        }

        if (!validator.isValidPassword(authenticationDTO.password)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No valid password provided"
            );
        }

        Optional<CustomUser> customUser = userDAO.findByEmail(authenticationDTO.email);

        if (customUser.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Can not register with this email"
            );
        }
        String encodedPassword = passwordEncoder.encode(authenticationDTO.password);

        CustomUser registeredCustomUser = new CustomUser(authenticationDTO.name, authenticationDTO.infix, authenticationDTO.lastName, authenticationDTO.email, encodedPassword, "user");
        userDAO.save(registeredCustomUser);
        String token = jwtUtil.generateToken(registeredCustomUser.getEmail(), registeredCustomUser.getAuthority());
        LoginResponse loginResponse = new LoginResponse(registeredCustomUser.getEmail(), token, registeredCustomUser.getAuthority());
        return ResponseEntity.ok(loginResponse);
    }




    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthenticationDTO body) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.email, body.password);

            authManager.authenticate(authInputToken);

            Optional<CustomUser> customUser = userDAO.findByEmail(body.email);
            if (customUser.isPresent()){
                String token = jwtUtil.generateToken(customUser.get().getEmail(), customUser.get().getAuthority());
                LoginResponse loginResponse = new LoginResponse(customUser.get().getEmail(), token, customUser.get().getAuthority());
                return ResponseEntity.ok(loginResponse);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden");
            }

        } catch (AuthenticationException authExc) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No valid credentials"
            );
        }
    }

    @GetMapping("/user")
    public ResponseEntity<CustomUser> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<CustomUser> customUser = userDAO.findByEmail(userEmail);
        if (customUser.isPresent()){
            return ResponseEntity.ok(customUser.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden");
        }
    }


}
