package com.changbenny.simpleecommerce.controller;

import com.changbenny.simpleecommerce.constant.ApiCode;
import com.changbenny.simpleecommerce.dto.ApiResponse;
import com.changbenny.simpleecommerce.dto.FdaNewsDTO;
import com.changbenny.simpleecommerce.dto.FdaNewsQueryParams;
import com.changbenny.simpleecommerce.service.FdaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fda")
@Tag(name = "食藥署新聞 API", description = "串接衛生福利部食品藥物管理署新聞資料")
public class FdaController {

    private static final Logger logger = LoggerFactory.getLogger(FdaController.class);

    @Autowired
    private FdaService fdaService;

    @GetMapping("/news") // 確保 @GetMapping 沒有被註釋掉
    @Operation(summary = "查詢食藥署新聞", description = "可依關鍵字、日期區間查詢新聞，最多回傳 1000 筆")
    public ResponseEntity<ApiResponse<List<FdaNewsDTO>>> searchNews(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String startdate,
            @RequestParam(required = false) String enddate
    ) {
        logger.info("查詢食藥署新聞: keyword={}, startdate={}, enddate={}", keyword, startdate, enddate);

        try {
            FdaNewsQueryParams fdaNewsQueryParams = new FdaNewsQueryParams();

            fdaNewsQueryParams.setKeyword(keyword);
            fdaNewsQueryParams.setStartdate(startdate);
            fdaNewsQueryParams.setEnddate(enddate);

            // 呼叫 Service 執行外部 API 存取
            List<FdaNewsDTO> newsList = fdaService.searchNews(fdaNewsQueryParams);

            return ResponseEntity.ok(
                    ApiResponse.success("查詢成功", newsList)
            );

        } catch (Exception e) {
            logger.error("查詢食藥署新聞失敗: {}", e.getMessage(), e);
            return ResponseEntity.ok(
                    ApiResponse.error(ApiCode.EXTERNAL_API_ERROR, "查詢失敗: " + e.getMessage())
            );
        }
    }
}