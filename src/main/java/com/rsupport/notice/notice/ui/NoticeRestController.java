package com.rsupport.notice.notice.ui;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rsupport.notice.auth.domain.AuthenticationPrincipal;
import com.rsupport.notice.auth.domain.LoginUser;
import com.rsupport.notice.notice.service.NoticeService;
import com.rsupport.notice.notice.dto.NoticeRequest;
import com.rsupport.notice.notice.dto.NoticeResponse;

@RestController
@RequestMapping("/api/notices")
public class NoticeRestController {
    private final NoticeService noticeService;

    public NoticeRestController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping
    public ResponseEntity<NoticeResponse> saveNoticeByPostMethod(@AuthenticationPrincipal LoginUser loginUser,
                                                                 @RequestBody NoticeRequest noticeRequest) {
        NoticeResponse noticeResponse = this.noticeService.saveNotice(noticeRequest, loginUser);
        return ResponseEntity.created(URI.create("/api/notices/" + noticeResponse.getId())).body(noticeResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponse> findNoticeByGetMethod(@AuthenticationPrincipal LoginUser loginUser,
                                                                @PathVariable("id") Long id) {
        return ResponseEntity.ok(this.noticeService.findNoticeBy(id, loginUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticeResponse> updateNoticeByPutMethod(@AuthenticationPrincipal LoginUser loginUser,
                                                                  @PathVariable("id") Long id,
                                                                  @RequestBody NoticeRequest noticeRequest) {
        return ResponseEntity.ok(this.noticeService.updateNotice(id, noticeRequest, loginUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNoticeByDeleteMethod(@AuthenticationPrincipal LoginUser loginUser,
                                                             @PathVariable("id") Long id) {
        this.noticeService.deleteNotice(id, loginUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<NoticeResponse>> findAllNoticesByGetMethod(Pageable pageable) {
        return ResponseEntity.ok(this.noticeService.findAllNoticesWithPaging(pageable));
    }
}
