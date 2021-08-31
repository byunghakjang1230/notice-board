package com.rsupport.notice.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rsupport.notice.auth.domain.LoginUser;
import com.rsupport.notice.auth.dto.LoginRequest;
import com.rsupport.notice.auth.dto.LoginResponse;
import com.rsupport.notice.auth.infrastructure.JwtTokenProvider;
import com.rsupport.notice.member.domain.Member;
import com.rsupport.notice.member.domain.MemberRepository;

@DisplayName("로그인 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtTokenProvider provider;

    @Test
    @DisplayName("사용자 로그인")
    void login() {
        // given
        LoginRequest loginRequest = new LoginRequest("user@email.com", "1234");
        Member member = new Member(loginRequest.getEmail(), loginRequest.getPassword());
        given(this.memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        given(provider.createToken(anyString())).willReturn("123123123");

        // when
        LoginResponse login = authService.login(loginRequest);

        // then
        assertThat(login.getToken()).isEqualTo("123123123");
    }

    @Test
    @DisplayName("토큰으로 사용자 찾기")
    void find_member_by_token() {
        Member member = new Member("user@email.com", "123123");
        given(this.provider.getEmailBy(anyString())).willReturn(member.getEmail());
        given(this.memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        // when
        LoginUser loginUser = authService.findMemberByToken("123123123");

        // then
        assertThat(loginUser.getEmail()).isEqualTo(member.getEmail());
    }
}
