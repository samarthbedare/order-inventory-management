package com.project.orderinventorymanagement.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AdminUserRepository adminRepo;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, AdminUserRepository adminRepo, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public AdminUser signup(@RequestBody AdminUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return adminRepo.save(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (authentication.isAuthenticated()) {
            String token = jwtUtil.generateToken(username);
            return Map.of(
                "token", token,
                "username", username,
                "role", "ADMIN"
            );
        } else {
            throw new RuntimeException("Authentication failed");
        }
    }
}
