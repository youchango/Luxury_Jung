package com.luxury.jung.domain.auth.service;

import com.luxury.jung.domain.auth.dto.LoginRequestDto;
import com.luxury.jung.domain.auth.dto.SignUpRequestDto;
import com.luxury.jung.domain.auth.dto.TokenResponseDto;
import com.luxury.jung.domain.member.entity.Member;
import com.luxury.jung.domain.member.repository.MemberRepository;
import com.luxury.jung.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signUp(SignUpRequestDto requestDto) {
        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 비밀번호 단방향 암호화 처리 후 DB 저장
        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(requestDto.getRole())
                .build();
                
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public TokenResponseDto login(LoginRequestDto requestDto) {
        // 유저 확인
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일이거나 비밀번호가 틀렸습니다."));

        // 비밀번호(해시) 일치 여부 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("가입되지 않은 이메일이거나 비밀번호가 틀렸습니다.");
        }

        // 인증 객체 구현 및 JWT 발급
        UserDetails userDetails = User.builder()
            .username(member.getEmail())
            .password(member.getPassword())
            .roles(member.getRole().name())
            .build();
            
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        String accessToken = jwtTokenProvider.createToken(authentication);
        
        return new TokenResponseDto(accessToken);
    }
}
