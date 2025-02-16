package com.example.gamewebshop.services;

import com.example.gamewebshop.dao.UserRepository;
import com.example.gamewebshop.models.CustomUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userDAO;

    public UserService(UserRepository userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<CustomUser> customUser = userDAO.findByEmail(email);
        if (customUser.isPresent()){
            return new User(
                    email,
                    customUser.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden");
        }

    }
}
