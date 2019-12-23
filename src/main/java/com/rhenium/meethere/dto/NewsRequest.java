package com.rhenium.meethere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @Size(max = 40, message = "新闻标题不能超过40字")
    private String newsTitle;

    // 已通过aop层校验
    private Integer adminId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime newsPostTime;

    @Size(max = 200, message = "新闻内容不能超过200字")
    private String newsContent;
}
