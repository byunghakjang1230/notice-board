package com.rsupport.notice.service;

import org.springframework.stereotype.Service;

import com.rsupport.notice.domain.Notice;
import com.rsupport.notice.domain.NoticeRepository;
import com.rsupport.notice.dto.NoticeRequest;
import com.rsupport.notice.dto.NoticeResponse;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public NoticeResponse saveNotice(NoticeRequest noticeRequest) {
        Notice savedNotice = this.noticeRepository.save(noticeRequest.toNotice());
        return NoticeResponse.of(savedNotice);
    }
}
