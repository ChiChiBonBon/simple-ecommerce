package com.changbenny.simpleecommerce.service;

import com.changbenny.simpleecommerce.dto.FdaNewsDTO;
import com.changbenny.simpleecommerce.dto.FdaNewsQueryParams;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service // 確保這是 Spring Service Bean
public class FdaService {

    @Value("${fda.api.url}")
    private String FDA_BASE_URL;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // 依賴注入 RestTemplate 和 ObjectMapper
    public FdaService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // 查詢食藥署新聞
    public List<FdaNewsDTO> searchNews(FdaNewsQueryParams queryParams) {

        // 1. 建構帶有查詢參數的完整 URL
        String url = UriComponentsBuilder.fromUriString(FDA_BASE_URL)
                .queryParam("keyword", queryParams.getKeyword())
                .queryParam("startdate", queryParams.getStartdate())
                .queryParam("enddate", queryParams.getEnddate())
                .build()
                .toUriString();

        // 2. 設定 User-Agent Headers (解決 403 錯誤)
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String rawJsonString;

        try {
            // 3. 呼叫外部 API，並將回傳結果作為 String 獲取 (解決 Content-Type 錯誤)
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                rawJsonString = response.getBody();
            } else {
                // 拋出 Exception，讓 Controller 的 try-catch 處理
                throw new RuntimeException("FDA API returned non-successful status: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException e) {
            // 處理 4xx 錯誤 (包含 403 Forbidden)
            throw new RuntimeException("Failed to access FDA API (" + e.getRawStatusCode() + "): " + e.getStatusText(), e);
        } catch (Exception e) {
            // 處理其他連線或網路錯誤
            throw new RuntimeException("Network error during FDA API call: " + e.getMessage(), e);
        }

        // 4. 手動解析 JSON 字串
        try {
            // 解析 JSON 陣列到 List<FdaNewsDTO>
            return objectMapper.readValue(
                    rawJsonString,
                    new TypeReference<List<FdaNewsDTO>>() {}
            );

        } catch (Exception e) {
            // 處理 JSON 解析失敗
            throw new RuntimeException("Failed to parse FDA API response: " + e.getMessage(), e);
        }
    }
}