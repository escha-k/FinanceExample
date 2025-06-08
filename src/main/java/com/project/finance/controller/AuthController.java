package com.project.finance.controller;

import com.project.finance.domain.Member;
import com.project.finance.dto.AuthDto;
import com.project.finance.security.TokenProvider;
import com.project.finance.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<Member> signUp(@RequestBody AuthDto.SignUp request) {
        Member result = memberService.register(request);

        log.info("new user registered: {}", result.getUsername());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody AuthDto.SignIn request) {
        Member member = memberService.authenticate(request);
        log.info("roles: {}", member.getRoles());
        String token = tokenProvider.generateToken(member.getUsername(), member.getRoles());

        log.info("user login: {}", request.getUsername());

        return ResponseEntity.ok(token);
    }
}
