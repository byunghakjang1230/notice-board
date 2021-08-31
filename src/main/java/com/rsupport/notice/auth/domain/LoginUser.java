package com.rsupport.notice.auth.domain;

import java.util.Objects;

public class LoginUser {
    private Long id;
    private String email;

    public LoginUser() {
    }

    public LoginUser(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isGuestUser() {
        return Objects.isNull(this.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginUser loginUser = (LoginUser) o;
        return Objects.equals(id, loginUser.id) && Objects.equals(email, loginUser.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
