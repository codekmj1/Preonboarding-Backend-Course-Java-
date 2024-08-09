package com.example.onboarding.service;

import java.util.Collections;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.onboarding.dto.request.LoginRequest;
import com.example.onboarding.dto.request.SignupRequest;
import com.example.onboarding.dto.response.AuthorityResponse;
import com.example.onboarding.dto.response.LoginResponse;
import com.example.onboarding.dto.response.MemberResponse;
import com.example.onboarding.model.Authority;
import com.example.onboarding.model.Member;
import com.example.onboarding.repository.MemberRepository;
import com.example.onboarding.security.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponse login(LoginRequest request) throws Exception {
        Member member = memberRepository.findByUsername(request.getUsername()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }       
        String token = jwtProvider.createToken(member.getUsername(), member.getRoles());
        return new LoginResponse(token);
    }

    public MemberResponse register(SignupRequest request) throws Exception {
        if (memberRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new Exception("이미 존재하는 사용자입니다.");
        }
        try {
            Member member = Member.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .nickname(request.getNickname())
                    .roles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()))
                    .build();
            memberRepository.save(member);
            return MemberResponse.builder()
                    .username(member.getUsername())
                    .nickname(member.getNickname())
                    .authorities(Collections.singletonList(new AuthorityResponse("ROLE_USER"))) 
                    .build();
        } catch (DataIntegrityViolationException e) {
            throw new Exception("데이터베이스 오류입니다."); 
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
    }
	
}
