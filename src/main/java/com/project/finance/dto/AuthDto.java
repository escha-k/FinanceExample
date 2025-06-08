package com.project.finance.dto;

import com.project.finance.domain.Member;
import lombok.Data;

import java.util.List;

public class AuthDto {

    @Data
    public static class SignIn {

        private String username;
        private String password;
    }

    @Data
    public static class SignUp {

        private String username;
        private String password;
        private List<String> roles;

        public Member toEntity() {
            return Member.builder()
                    .username(username)
                    .password(password)
                    .roles(roles)
                    .build();
        }
    }
}
