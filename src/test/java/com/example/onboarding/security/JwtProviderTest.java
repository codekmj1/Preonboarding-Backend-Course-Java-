package com.example.onboarding.security;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.onboarding.model.Authority;
import com.example.onboarding.model.JpaUserDetailsService;
import org.springframework.test.context.TestPropertySource;
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
public class JwtProviderTest {

    @Mock
    private JpaUserDetailsService userDetailsService;

    @InjectMocks
    private JwtProvider jwtProvider;

    @Value("${jwt.secret.key}")
    private String secretKey = "testSecretKey"; // 테스트 비밀 키

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtProvider.setSalt("XzN1YjY3OTYwMDY4YjQ1ZGUwOTc2YzY5ZjE1MjU2ZjZlNzY4YzYwYjM5YjE3"); // 직접 비밀 키 설정
        jwtProvider.init();
    }

    @Test
    public void testCreateToken() {
        String username = "testUser";
        Authority authority = Authority.builder().name("ROLE_USER").build();
        List<Authority> roles = Collections.singletonList(authority);
        String token = jwtProvider.createToken(username, roles);
        assertNotNull(token);
        assertTrue(token.startsWith("ey")); 
    }

    @Test
    public void testGetUsername() {
        String username = "testUser";
        Authority authority = Authority.builder().name("ROLE_USER").build();
        List<Authority> roles = Collections.singletonList(authority);
        String token = jwtProvider.createToken(username, roles);
        String extractedUsername = jwtProvider.getUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    public void testValidateToken() {
        String username = "testUser";
        Authority authority = Authority.builder().name("ROLE_USER").build();
        List<Authority> roles = Collections.singletonList(authority);
        String token = jwtProvider.createToken(username, roles);
        boolean isValid = jwtProvider.validateToken("BEARER " + token);
        assertTrue(isValid);
    }


    @Test
    public void testInvalidToken() {
        String invalidToken = "BEARER invalidToken";
        boolean isValid = jwtProvider.validateToken(invalidToken);
        assertFalse(isValid);
    }
}