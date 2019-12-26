package com.rhenium.meethere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/16 23:06
 */
@Data
@AllArgsConstructor
@Builder
public class StadiumRequest {
    private Integer stadiumId;

    @Size(max = 40, message = "场馆名不能超过40字")
    private String stadiumName;

    private String typeName;

    private Integer type;

    @Size(max = 100, message = "位置信息不能超过100字")
    private String location;

    private String description;
    private BigDecimal price;
    private String picture;
    private String pictureRaw;

    // 校验
    private Integer adminId;
}
