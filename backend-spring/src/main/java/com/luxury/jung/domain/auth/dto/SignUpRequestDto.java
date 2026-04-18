package com.luxury.jung.domain.auth.dto;

import com.luxury.jung.domain.member.entity.Role;
import lombok.Data;

@Data
public class SignUpRequestDto {
    private String email;
    private String password;
    // 관리자(ADMIN) 혹은 유저(USER) 여부 (향후 보안을 위해 어드민 가입 비밀키 등을 프론트에서 추가로 요구할 수 있습니다)
    private Role role; 
}
