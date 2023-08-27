package com.oceans7.dib.domain.mypage.dto.response;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.entity.Dib;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DibResponseDto {

    @Schema(description = "컨텐츠 아이디", example = "123492")
    private Long contentId;

    @Schema(description = "컨텐츠 타입", example = "TOURIST_SPOT")
    private ContentType contentType;

    @Schema(description = "장소 이름", example = "해남문학관")
    private String title;

    @Schema(description = "장소 주소", example = "전라남도 해남군 해남읍 해남문학로 1")
    private String address;

    @Schema(description = "전화번호", example = "061-532-1000")
    private String tel;

    @Schema(description = "썸네일 URL", example = "http://tong.visitkorea.or.kr/cms/resource/06/2510606_image2_1.jpg")
    private String firstImageUrl;

    public static DibResponseDto of(Dib dib) {
        DibResponseDto dibResponse = new DibResponseDto();
        dibResponse.contentId = dib.getContentId();
        dibResponse.contentType = ContentType.getContentTypeByCode(dib.getContentTypeId());
        dibResponse.title = dib.getTitle();
        dibResponse.address = dib.getAddress();
        dibResponse.tel = dib.getTel();
        dibResponse.firstImageUrl = dib.getFirstImage();

        return dibResponse;
    }
}
