package com.oceans7.dib.domain.custom_content.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_content_id")
    private Long customContentId;

    @Column(name = "json_content", length = 3000)
    private String jsonContent;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "sub_title")
    private String subTitle;

    public static CustomContent of(String jsonContent, String coverImageUrl, String title, String subTitle) {
        CustomContent customContent = new CustomContent();

        customContent.jsonContent = jsonContent;
        customContent.coverImageUrl = coverImageUrl;
        customContent.title = title;
        customContent.subTitle = subTitle;

        return customContent;
    }
}
