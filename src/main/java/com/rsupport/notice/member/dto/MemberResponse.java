package com.rsupport.notice.member.dto;

import com.rsupport.notice.member.domain.Member;

public class MemberResponse {
    private Long id;
    private String email;

    protected MemberResponse() {
    }

    private MemberResponse(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
