package com.rsupport.notice.domain;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findByIdAndDeletedIsFalse(Long id);
    Page<Notice> findAllByDeletedIsFalse(Pageable pageable);
}
