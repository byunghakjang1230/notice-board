package com.rsupport.notice.auth.service;

import org.springframework.stereotype.Service;

import com.rsupport.notice.auth.domain.LoginUser;
import com.rsupport.notice.auth.dto.LoginRequest;
import com.rsupport.notice.auth.dto.LoginResponse;
import com.rsupport.notice.auth.exception.AuthorizationException;
import com.rsupport.notice.auth.infrastructure.JwtTokenProvider;
import com.rsupport.notice.member.domain.Member;
import com.rsupport.notice.member.domain.MemberRepository;
import com.rsupport.notice.member.exception.MemberNotFoundException;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new AuthorizationException("조회된 사용자가 없습니다."));
        validateMatchedPassword(member, loginRequest);
        return new LoginResponse(jwtTokenProvider.createToken(loginRequest.getEmail()));
    }

    private void validateMatchedPassword(Member member, LoginRequest loginRequest) {
        if(!member.checkPassword(loginRequest.getPassword())){
            throw new AuthorizationException("비밀변호가 맞지 않습니다.");
        }
    }

    public LoginUser findMemberByToken(String token) {
        String email = jwtTokenProvider.getEmailBy(token);
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        return new LoginUser(member.getId(), member.getEmail());
    }
}
