package com.rsupport.notice.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rsupport.notice.member.domain.Member;
import com.rsupport.notice.member.domain.MemberRepository;
import com.rsupport.notice.member.dto.MemberRequest;
import com.rsupport.notice.member.dto.MemberResponse;

@DisplayName("MemberService 단우 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 기능")
    void join_member() {
        // given
        MemberRequest memberRequest = new MemberRequest("user@email.com", "123");
        Member member = new Member(memberRequest.getEmail(), memberRequest.getPassword());
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(memberRepository.existsByEmail(anyString())).willReturn(false);

        // when
        MemberResponse memberResponse = this.memberService.joinMember(memberRequest);

        // then
        assertThat(memberResponse).isNotNull();
    }
}
