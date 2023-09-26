package com.oceans7.dib.domain.report.dto.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ReportRequestDto {
    private String organismName;

    private String foundLocation;

    private List<String> imageUrlList;
}
