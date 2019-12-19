package com.rhenium.meethere.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HavenTong
 * @date 2019/11/22 5:25 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News implements Serializable {
    private static final long serialVersionUID = 7620744559347674335L;

    private Integer newsId;
    private String newsTitle;
    private Integer adminId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime newsPostTime;
    private String newsContent;

    // 相关联的管理员
    private Admin admin;
}
