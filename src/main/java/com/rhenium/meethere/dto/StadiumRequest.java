package com.rhenium.meethere.dto;

import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.enums.StadiumTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
    private Integer typeCode;
    private String typeName;
    private String location;
    private String description;
    private double price;
    private String picture;

    public StadiumRequest(Stadium stadium) {
        this(stadium.getStadiumId(), stadium.getStadiumName(), stadium.getType(), StadiumTypeEnum.getByCode(stadium.getType()).getType(), stadium.getLocation(), stadium.getDescription(), stadium.getPrice(), stadium.getPicture());
    }
}
