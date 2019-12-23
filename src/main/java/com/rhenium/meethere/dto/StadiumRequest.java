package com.rhenium.meethere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
    private String stadiumName;
    private String typeName;
    private String location;
    private String description;
    private BigDecimal price;
    private String picture;
}
