package com.example.gamewebshop.dto;

public class LoginResponse {
    public String email;
    public String authority;
    public String token;


    public LoginResponse(String email, String token, String authority) {
        this.email = email;
        this.token = token;
        this.authority = authority;
    }
}
