package com.example.onboarding.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onboarding.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> findByUsername(String username);
}
