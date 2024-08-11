package com.example.onboarding.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.onboarding.dto.request.LoginRequest;
import com.example.onboarding.dto.request.SignupRequest;
import com.example.onboarding.dto.response.LoginResponse;
import com.example.onboarding.dto.response.SignupResponse;
import com.example.onboarding.model.Member;
import com.example.onboarding.repository.MemberRepository;
import com.example.onboarding.security.JwtProvider;

public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    private Member member;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        member = Member.builder()
                .username("testUser")
                .password("encodedPassword")
                .nickname("Test User")
                .build();
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void testLogin_Success() throws Exception {
        LoginRequest request = new LoginRequest("testUser", "plainPassword");
        when(memberRepository.findByUsername("testUser")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);
        when(jwtProvider.createToken("testUser", member.getRoles())).thenReturn("token");
        LoginResponse response = memberService.login(request);
        System.out.println("Login Success: " + response.getToken());
        assertEquals("token", response.getToken());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void testLogin_Failure_BadCredentials() {
        LoginRequest request = new LoginRequest("testUser", "wrongPassword");
        when(memberRepository.findByUsername("testUser")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);
        Exception exception = assertThrows(BadCredentialsException.class, () -> memberService.login(request));
        System.out.println("Login Failure: " + exception.getMessage()); 
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void testRegister_Success() throws Exception {
        SignupRequest request = new SignupRequest(1L, "newUser", "plainPassword", "New User");
        when(memberRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        SignupResponse response = memberService.register(request);
        System.out.println("Register Success: " + response.getUsername() + ", " + response.getNickname());
        assertEquals("newUser", response.getUsername());
        assertEquals("New User", response.getNickname());
    }

    @Test
    @DisplayName("회원가입 실패 테스트")
    public void testRegister_Failure_UserAlreadyExists() {
        SignupRequest request = new SignupRequest(1L ,"testUser", "plainPassword", "Test User");
        when(memberRepository.findByUsername("testUser")).thenReturn(Optional.of(member));
        Exception exception = assertThrows(Exception.class, () -> memberService.register(request));
        System.out.println("Register Failure: " + exception.getMessage());
    }
}
