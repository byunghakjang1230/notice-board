package com.rsupport.notice.member.domain;

import java.util.Objects;

import javax.persistence.*;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;

    protected Member() {
    }

    public Member(String email, String password) {
        validateEmailIsNullOrEmpty(email);
        validatePasswordIsNullOrEmpty(password);
        this.email = email;
        this.password = password;
    }

    public static Member createGuestMember(){
        return new Member();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id)
                && Objects.equals(email, member.email)
                && Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }

    private void validateEmailIsNullOrEmpty(String email) {
        if (Objects.isNull(email) || email.isEmpty()) {
            throw new IllegalArgumentException("Email이 Null이거나 Empty입니다.");
        }
    }

    private void validatePasswordIsNullOrEmpty(String password) {
        if (Objects.isNull(password) || password.isEmpty()) {
            throw new IllegalArgumentException("Password가 Null이거나 Empty입니다.");
        }
    }
}
