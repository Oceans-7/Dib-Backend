package com.oceans7.dib.domain.report.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ReportRequestDto {
    private String organismName;

    private String foundLocation;

    private List<String> imageUrlList;
}
