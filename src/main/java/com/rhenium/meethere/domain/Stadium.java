package com.rhenium.meethere.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author HavenTong
 * @date 2019/11/22 5:26 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stadium implements Serializable {
    private static final long serialVersionUID = -4454785377509579955L;

    private Integer stadiumId;
    private String stadiumName;
    // 后续改为枚举类型
    private Integer type;
    private String location;
    private String description;
    private Double price;
    private String picture;
}
