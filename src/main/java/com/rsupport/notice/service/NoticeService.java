package com.rsupport.notice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rsupport.notice.domain.Notice;
import com.rsupport.notice.domain.NoticeRepository;
import com.rsupport.notice.dto.NoticeRequest;
import com.rsupport.notice.dto.NoticeResponse;
import com.rsupport.notice.exception.NotFoundNoticeException;
import com.rsupport.notice.exception.NoticePermissionDeniedException;

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

    public NoticeResponse findNoticeBy(Long id) {
        return NoticeResponse.of(findNoticeOrThrow(id));
    }

    public NoticeResponse updateNotice(Long id, NoticeRequest updateNoticeRequest) {
        Notice findNotice = findNoticeOrThrow(id);
        validateNoticePermission(findNotice, updateNoticeRequest.getWriter());
        findNotice.updateTo(updateNoticeRequest.toNotice());
        return NoticeResponse.of(findNotice);
    }

    public void deleteNotice(long id, NoticeRequest deleteNoticeRequest) {
        Notice findNotice = findNoticeOrThrow(id);
        validateNoticePermission(findNotice, deleteNoticeRequest.getWriter());
        this.noticeRepository.delete(findNotice);
    }

    private Notice findNoticeOrThrow(Long id) {
        return this.noticeRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundNoticeException("요청한 공지사항이 없습니다."));
    }

    private void validateNoticePermission(Notice findNotice, String writer) {
        if (!findNotice.hasSameOwner(writer)) {
            throw new NoticePermissionDeniedException("공지사항 수정 권한이 없습니다.");
        }
    }

    public Page<NoticeResponse> findAllNoticesWithPaging(Pageable pageable) {
        return this.noticeRepository.findAllByDeletedIsFalse(pageable)
                .map(NoticeResponse::of);
    }
}
