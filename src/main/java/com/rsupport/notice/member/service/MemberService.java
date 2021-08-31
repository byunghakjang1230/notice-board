package com.rsupport.notice.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rsupport.notice.member.domain.Member;
import com.rsupport.notice.member.domain.MemberRepository;
import com.rsupport.notice.member.dto.MemberRequest;
import com.rsupport.notice.member.dto.MemberResponse;
import com.rsupport.notice.member.exception.EmailExistsException;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse joinMember(final MemberRequest memberRequest) {
        Member member = new Member(memberRequest.getEmail(), memberRequest.getPassword());
        validateExistsEmail(member.getEmail());
        return MemberResponse.of(this.memberRepository.save(member));
    }

    private void validateExistsEmail(final String email) {
        if (this.memberRepository.existsByEmail(email)) {
            throw new EmailExistsException("이미 사용중인 이메일입니다.");
        }
    }
}
