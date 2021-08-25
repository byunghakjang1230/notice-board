package com.rsupport.notice.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rsupport.notice.service.NoticeService;
import com.rsupport.notice.dto.NoticeRequest;
import com.rsupport.notice.dto.NoticeResponse;

@RestController
@RequestMapping("/api/notices")
public class NoticeRestController {
    private final NoticeService noticeService;

    public NoticeRestController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping
    public ResponseEntity<NoticeResponse> registerNotice(@RequestBody NoticeRequest noticeRequest) {
        NoticeResponse noticeResponse = this.noticeService.saveNotice(noticeRequest);
        return ResponseEntity.created(URI.create("/api/notices/" + noticeResponse.getId())).body(noticeResponse);
    }
}
