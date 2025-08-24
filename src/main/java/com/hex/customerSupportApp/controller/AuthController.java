package com.hex.customerSupportApp.controller;

import com.hex.customerSupportApp.dto.AuthDtos;
import com.hex.customerSupportApp.repository.UserRepository;
import com.hex.customerSupportApp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthDtos.AuthResponse> register(@Valid @RequestBody AuthDtos.RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.AuthResponse> login(@Valid @RequestBody AuthDtos.LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthDtos.MeResponse> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("satyam");
        System.out.println(auth.getName());


        var user = userRepository.findByUsername(auth.getName()).orElseThrow();
        System.out.println("satyam");
        return ResponseEntity.ok(new AuthDtos.MeResponse(
                user.getId(), user.getUsername(), user.getEmail(), user.getRole().name()));
    }

}
