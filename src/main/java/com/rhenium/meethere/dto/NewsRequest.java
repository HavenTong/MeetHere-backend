package com.rhenium.meethere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author HavenTong
 * @date 2019/12/19 4:19 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsRequest {
    private Integer newsId;
    private String newsTitle;
    private Integer adminId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime newsPostTime;

    private String newsContent;
}
