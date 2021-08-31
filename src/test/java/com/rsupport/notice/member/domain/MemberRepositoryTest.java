package com.rsupport.notice.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void existsBy_email() {
        // given
        String email = "user@email.com";
        this.memberRepository.save(new Member(email, "123"));

        // when
        boolean exists = this.memberRepository.existsByEmail(email);

        // then
        assertThat(exists).isTrue();
    }
}
