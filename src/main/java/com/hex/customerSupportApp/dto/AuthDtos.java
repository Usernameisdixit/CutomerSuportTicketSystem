package com.hex.customerSupportApp.dto;

import com.hex.customerSupportApp.entity.Role;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public record  RegisterRequest(

         @NotBlank String username,
         @Email String email,
         @Size(min=6) String password,
         @NotNull Role role
    ){}


    public record LoginRequest(@NotBlank String username,@NotBlank String password){}

    public record  AuthResponse(String token,String username,String role){}

    public record MeResponse(Long id,String username,String email,String role){}



}
