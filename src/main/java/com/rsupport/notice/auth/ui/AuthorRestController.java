package com.rsupport.notice.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rsupport.notice.auth.dto.LoginRequest;
import com.rsupport.notice.auth.dto.LoginResponse;
import com.rsupport.notice.auth.service.AuthService;

@RestController
public class AuthorRestController {
    private final AuthService authService;

    public AuthorRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok().body(this.authService.login(loginRequest));
    }
}

