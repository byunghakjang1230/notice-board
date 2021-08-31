package com.rsupport.notice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rsupport.notice.member.domain.Member;
import com.rsupport.notice.member.domain.MemberRepository;
import com.rsupport.notice.notice.domain.Notice;
import com.rsupport.notice.notice.domain.NoticeRepository;

@Component
public class DataLoaderConfig implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;

    public DataLoaderConfig(MemberRepository memberRepository, NoticeRepository noticeRepository) {
        this.memberRepository = memberRepository;
        this.noticeRepository = noticeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Member member = memberRepository.save(new Member("user@email.com", "123"));
        List<Notice> notices = new ArrayList<>();
        for (int i = 1; i <= 130; i++) {
            notices.add(new Notice("title" + i, "content" + i, member));
        }
        noticeRepository.saveAll(notices);
    }
}
