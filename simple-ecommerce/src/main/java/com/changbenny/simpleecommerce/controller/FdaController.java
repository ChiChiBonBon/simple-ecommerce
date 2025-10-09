package com.changbenny.simpleecommerce.controller;

import com.changbenny.simpleecommerce.constant.ApiCode;
import com.changbenny.simpleecommerce.dto.ApiResponse;
import com.changbenny.simpleecommerce.dto.FdaNewsDTO;
import com.changbenny.simpleecommerce.dto.FdaNewsQueryParams;
import com.changbenny.simpleecommerce.dto.FdaNewsResponse;
import com.changbenny.simpleecommerce.service.FdaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/fda")
@Tag(name = "食藥署新聞 API", description = "串接衛生福利部食品藥物管理署新聞資料")
public class FdaController {

    private static final Logger logger = LoggerFactory.getLogger(FdaController.class);

    @Autowired
    private FdaService fdaService;

    @GetMapping("/news")
    public ResponseEntity<ApiResponse<List<FdaNewsResponse>>> searchNews(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "") String startdate,
            @RequestParam(required = false, defaultValue = "") String enddate
    ) {
        logger.info("查詢食藥署新聞: keyword={}, startdate={}, enddate={}", keyword, startdate, enddate);
        try {
            var query = new FdaNewsQueryParams();
            query.setKeyword(keyword);
            query.setStartdate(startdate);
            query.setEnddate(enddate);

            // 目前 service 回來是「吃外部中文鍵的 DTO」
            List<FdaNewsDTO> in = fdaService.searchNews(query);

            // 控制器先轉一層給前端（英文鍵/attachments 陣列/日期字串）
            List<FdaNewsResponse> payload = in.stream().map(dto -> {
                var r = new FdaNewsResponse();
                r.setTitle(dto.getTitle());
                r.setContent(dto.getContent());
                r.setContentText(dto.getContent()!=null
                        ? dto.getContent().replaceAll("<br\\s*/?>", "\n")
                        .replaceAll("<[^>]+>", "")
                        .replaceAll("\\s+"," ")
                        .trim()
                        : "");
                r.setAttachments(dto.getAttachments()==null ? List.of()
                        : Arrays.stream(dto.getAttachments().split(","))
                        .map(String::trim).filter(s -> !s.isEmpty()).toList());
                r.setPublishDate(dto.getPublishDate());
                return r;
            }).toList();

            return ResponseEntity.ok(ApiResponse.success("查詢成功", payload));
        } catch (Exception e) {
            logger.error("查詢食藥署新聞失敗", e);
            return ResponseEntity.ok(ApiResponse.error(ApiCode.EXTERNAL_API_ERROR, "查詢失敗: " + e.getMessage()));
        }
    }
}