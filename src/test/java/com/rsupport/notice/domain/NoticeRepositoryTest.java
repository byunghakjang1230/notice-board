package com.rsupport.notice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * NoticeRepository 테스트 코드 작성
 */
@DisplayName("NoticeRepository 테스트")
@DataJpaTest
class NoticeRepositoryTest {
    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("JPA 기본 동작 확인")
    void default_save() {
        // when
        Notice saveNotice = noticeRepository.save(new Notice("hi", "register test", "user@email.com"));

        // then
        assertThat(saveNotice).isNotNull();
    }
}
