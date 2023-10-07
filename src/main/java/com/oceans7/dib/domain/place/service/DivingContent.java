package com.oceans7.dib.domain.place.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum DivingContent {
    DIVING_01(131913L),
    DIVING_02(2710817L),
    DIVING_03(131498L),
    DIVING_04(2755013L),
    DIVING_05(2773417L),
    DIVING_06(636073L),
    DIVING_07(2820293L),
    ;

    DivingContent(Long contentId) {
        this.contentId = contentId;
    }
    private Long contentId;

    // Long 타입의 contentId와 매칭하는 enum 값을 반환하는 함수
    public static boolean isDivingContent(Long contentId) {
        return Stream.of(values())
                .anyMatch(d -> d.contentId.equals(contentId));
       }

    // enum의 contentId를 Long 타입의 리스트로 반환하는 함수
    public static List<Long> getAllContentIds() {
        return Stream.of(values())
                .map(d -> d.contentId)
                .collect(Collectors.toList());
    }
}
