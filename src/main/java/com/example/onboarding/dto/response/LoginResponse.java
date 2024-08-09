package com.example.onboarding.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
@Schema(description = "로그인 응답")
public class LoginResponse {
    private String token;
}
