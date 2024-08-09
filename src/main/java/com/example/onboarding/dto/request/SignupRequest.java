package com.example.onboarding.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupRequest {

	private Long id;
	private String username;
	private String password;
	private String nickname;

}
