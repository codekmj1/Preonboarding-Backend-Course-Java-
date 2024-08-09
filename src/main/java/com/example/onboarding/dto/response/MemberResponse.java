package com.example.onboarding.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private String username;
    private String nickname;
    private List<AuthorityResponse> authorities; 
}
