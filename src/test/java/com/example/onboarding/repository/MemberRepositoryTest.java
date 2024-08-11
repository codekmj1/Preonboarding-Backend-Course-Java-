package com.example.onboarding.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.example.onboarding.model.Member;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    public void setUp() {
        member = Member.builder()
                .username("asd")
                .password("123")
                .nickname("asdd")
                .build();
    }

    @AfterEach
    public void tearDown() {
        memberRepository.deleteAll(); 
    }

    @Test
    public void testSaveAndFindByUsername() {
        memberRepository.save(member);
        Optional<Member> foundMember = memberRepository.findByUsername(member.getUsername());
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getUsername()).isEqualTo(member.getUsername());
        assertThat(foundMember.get().getNickname()).isEqualTo(member.getNickname());
    }
}