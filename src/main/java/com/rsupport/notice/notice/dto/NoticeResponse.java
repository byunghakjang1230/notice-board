package com.rsupport.notice.notice.dto;

import java.time.LocalDate;

import com.rsupport.notice.member.domain.Member;
import com.rsupport.notice.member.dto.MemberResponse;
import com.rsupport.notice.notice.domain.Notice;

public class NoticeResponse {
    private Long id;
    private String title;
    private String content;
    private MemberResponse writer;
    private boolean editable;
    private LocalDate createDate;
    private LocalDate lastModifiedDate;

    protected NoticeResponse() {
    }

    private NoticeResponse(Long id, String title, String content, MemberResponse writer, boolean editable,
                           LocalDate createDate, LocalDate lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.editable = editable;
        this.createDate = createDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static NoticeResponse of(Long id, String title, String content, Member writer, boolean editable) {
        return new NoticeResponse(id, title, content, MemberResponse.of(writer), editable, null, null);
    }

    public static NoticeResponse of(Notice notice) {
        return new NoticeResponse(notice.getId(), notice.getTitle(), notice.getContent(),
                MemberResponse.of(notice.getWriter()), false, notice.getCreateDate(), notice.getLastModifiedDate());
    }

    public static NoticeResponse of(Notice notice, boolean editable) {
        return new NoticeResponse(notice.getId(), notice.getTitle(), notice.getContent(),
                MemberResponse.of(notice.getWriter()), editable, notice.getCreateDate(), notice.getLastModifiedDate());
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public MemberResponse getWriter() {
        return writer;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }
}
