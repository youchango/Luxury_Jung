package com.luxury.jung.global.security;

import com.luxury.jung.domain.member.entity.Member;
import com.luxury.jung.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 검증 필터나 AuthenticationProvider에서 DB 조회가 필요할 때 사용됩니다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("가입되지 않은 이메일입니다: " + email));

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().name()) // 예를들어 여기서 "ADMIN"을 넣으면 Spring Security는 내부적으로 "ROLE_ADMIN" 스코프로 처리합니다.
                .build();
    }
}
