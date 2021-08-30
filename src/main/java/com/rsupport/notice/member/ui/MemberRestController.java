package com.rsupport.notice.member.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rsupport.notice.member.dto.MemberRequest;
import com.rsupport.notice.member.dto.MemberResponse;
import com.rsupport.notice.member.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class MemberRestController {
    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> joinMemberByPostMethod(@RequestBody MemberRequest memberRequest) {
        MemberResponse memberResponse = this.memberService.joinMember(memberRequest);
        return ResponseEntity.created(URI.create("/api/members/" + memberResponse.getId())).body(memberResponse);
    }
}
