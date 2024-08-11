package com.example.onboarding.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SignupResponse {
    private String username;
    private String nickname;
    private List<AuthorityResponse> authorities; 	
}
