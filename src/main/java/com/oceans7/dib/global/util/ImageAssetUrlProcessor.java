package com.oceans7.dib.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class ImageAssetUrlProcessor {

    @Value("${cloud.aws.s3.hostDomain}")
    private String s3HostDomain;

    @Value("${cloud.aws.s3.directory.images}")
    private String s3ImageDirectory;

    @Value("${cloud.aws.cloudFront.hostDomain}")
    private String cloudFrontHostDomain;

    /**
     * 입력된 URL 문자열이 S3 호스트 도메인에 해당하는지 확인하고, 해당 URL의 경로(Path)를 추출합니다.
     *
     * @param input 입력 URL 문자열
     * @return URL이 S3 호스트 도메인에 해당하는 경우 경로(Path)를 반환하며, 그렇지 않은 경우 null을 반환합니다.
     */
    public String extractUrlPath(String input) {
        try {
            // 주어진 문자열을 URL 객체로 파싱
            URL url = new URL(input);

            // URL 객체에서 호스트 주소 추출
            String host = url.getProtocol() + "://" + url.getHost();

            // 대상 호스트 주소와 일치하는 경우에만 경로(Path) 추출
            if (host.equals(s3HostDomain)) {
                String path = url.getPath();
                return path;
            } else {
                // 대상 호스트 주소와 일치하지 않으면 null 반환
                return input;
            }
        } catch (MalformedURLException e) {
            // 유효한 URL이 아닌 경우 예외 처리
            return null; // 또는 예외 처리에 맞는 다른 동작 수행
        }
    }

    /**
     * 입력된 문자열이 '/images' 문자열로 시작하는지 확인하고, 그렇다면 CloudFront 호스트 도메인을 prefix로 붙여 반환합니다.
     *
     * @param input 입력 문자열
     * @return 디렉토리명 확인하고 CloudFront 호스트 도메인이 prefix로 붙은 문자열을 반환하며,
     * 그렇지 않은 경우 입력 문자열 그대로 반환합니다.
     */
    public String prependCloudFrontHost(String input) {
        if(input.startsWith(s3ImageDirectory)) {
            return cloudFrontHostDomain + input;
        } else {
            return input;
        }
    }

}
