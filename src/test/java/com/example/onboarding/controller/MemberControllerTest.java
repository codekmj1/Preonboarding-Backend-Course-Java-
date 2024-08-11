package com.example.onboarding.controller;

import com.example.onboarding.dto.request.LoginRequest;
import com.example.onboarding.dto.request.SignupRequest;
import com.example.onboarding.dto.response.LoginResponse;
import com.example.onboarding.dto.response.SignupResponse;
import com.example.onboarding.mock.WithCustomMockUser;
import com.example.onboarding.model.Authority;
import com.example.onboarding.model.Member;
import com.example.onboarding.repository.MemberRepository;
import com.example.onboarding.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private MemberRepository memberRepository;

    @Mock
    private MemberService memberService;

    @BeforeEach
    void setUp() {
    	memberRepository.deleteAll();
        Authority authority = Authority.builder().name("ROLE_USER").build();
        String encodedPassword = passwordEncoder.encode("password");
        Member member = Member.builder()
                .username("asd")
                .password(encodedPassword)
                .nickname("asdd")
                .roles(Collections.singletonList(authority))
                .build();
        memberRepository.save(member);
    }

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("asd");
        request.setPassword("password");
        LoginResponse response = new LoginResponse("mock-token");
        when(memberService.login(any(LoginRequest.class))).thenReturn(response);
        ResultActions resultActions = mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"asd\", \"password\":\"password\"}"))
                .andExpect(status().isOk());
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("로그인 테스트 성공: 사용자 asd로 로그인 성공, 응답 내용: " + responseContent);
    }

    @Test
    public void testSignupSuccess() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("qwe");
        request.setPassword("123");
        request.setNickname("qwee");
        SignupResponse response = SignupResponse.builder()
                .username("qwe")
                .nickname("qwee")
                .authorities(Collections.emptyList())
                .build();
        when(memberService.register(any(SignupRequest.class))).thenReturn(response);
        ResultActions resultActions = mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"qwe\", \"password\":\"123\", \"nickname\":\"qwee\"}"))
                .andExpect(status().isCreated());
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("회원가입 테스트 성공: 사용자 newUser로 가입 성공"+responseContent);
    }
}