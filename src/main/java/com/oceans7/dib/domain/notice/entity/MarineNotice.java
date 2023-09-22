package com.oceans7.dib.domain.notice.entity;

import com.oceans7.dib.global.base_entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarineNotice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @Column(name = "title")
    private String title;

    @Column(name = "category")
    private String category;

    @Column(name = "content", length = 1000)
    private String content;

    public static MarineNotice of(String title, String category, String content) {
        MarineNotice marineNotice = new MarineNotice();

        marineNotice.title = title;
        marineNotice.category = category;
        marineNotice.content = content;

        return marineNotice;
    }
}
