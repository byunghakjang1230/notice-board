package com.rsupport.notice.member.dto;

public class MemberRequest {
    private String email;
    private String password;

    protected MemberRequest() {
    }

    public MemberRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
