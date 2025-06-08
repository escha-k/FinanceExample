package com.project.finance.service;

import com.project.finance.domain.Member;
import com.project.finance.dto.AuthDto;
import com.project.finance.exception.impl.AlreadyExistsUserException;
import com.project.finance.exception.impl.PasswordNotMatchException;
import com.project.finance.exception.impl.UserNotFoundException;
import com.project.finance.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("could not find user: " + username));
    }

    public Member register(AuthDto.SignUp memberDto) {
        if (memberRepository.existsByUsername(memberDto.getUsername())) {
            throw new AlreadyExistsUserException();
        }

        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        return memberRepository.save(memberDto.toEntity());
    }

    public Member authenticate(AuthDto.SignIn memberDto) {
        Member member = memberRepository.findByUsername(memberDto.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
            throw new PasswordNotMatchException();
        }

        return member;
    }
}
