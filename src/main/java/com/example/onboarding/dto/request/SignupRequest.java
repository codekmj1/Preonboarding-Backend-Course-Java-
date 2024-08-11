package com.example.onboarding.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SignupRequest {

	private Long id;
	private String username;
	private String password;
	private String nickname;

}
