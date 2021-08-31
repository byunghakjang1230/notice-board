package com.rsupport.notice.notice.dto;

import java.time.format.DateTimeFormatter;

import com.rsupport.notice.member.domain.Member;
import com.rsupport.notice.member.dto.MemberResponse;
import com.rsupport.notice.notice.domain.Notice;

public class NoticeResponse {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Long id;
    private String title;
    private String content;
    private MemberResponse writer;
    private boolean editable;
    private String createDateTime;
    private String lastModifiedDateTime;

    protected NoticeResponse() {
    }

    private NoticeResponse(Long id, String title, String content, MemberResponse writer, boolean editable,
                           String createDateTime, String lastModifiedDateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.editable = editable;
        this.createDateTime = createDateTime;
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public static NoticeResponse of(Long id, String title, String content, Member writer, boolean editable) {
        return new NoticeResponse(id, title, content, MemberResponse.of(writer), editable, null, null);
    }

    public static NoticeResponse of(Notice notice) {
        return new NoticeResponse(notice.getId(), notice.getTitle(), notice.getContent(),
                MemberResponse.of(notice.getWriter()), false,
                notice.getCreateDateTime().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)),
                notice.getLastModifiedDateTime().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)));
    }

    public static NoticeResponse of(Notice notice, boolean editable) {
        return new NoticeResponse(notice.getId(), notice.getTitle(), notice.getContent(),
                MemberResponse.of(notice.getWriter()), editable,
                notice.getCreateDateTime().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)),
                notice.getLastModifiedDateTime().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)));
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

    public String getCreateDateTime() {
        return createDateTime;
    }

    public boolean isEditable() {
        return editable;
    }

    public String getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }
}
