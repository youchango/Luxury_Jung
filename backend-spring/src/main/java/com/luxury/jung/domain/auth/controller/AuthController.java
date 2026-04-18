package com.luxury.jung.domain.auth.controller;

import com.luxury.jung.domain.auth.dto.LoginRequestDto;
import com.luxury.jung.domain.auth.dto.SignUpRequestDto;
import com.luxury.jung.domain.auth.dto.TokenResponseDto;
import com.luxury.jung.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto requestDto) {
        authService.signUp(requestDto);
        return ResponseEntity.ok("가입이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        TokenResponseDto token = authService.login(requestDto);
        return ResponseEntity.ok(token);
    }
}
