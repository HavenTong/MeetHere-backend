package com.rhenium.meethere.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author HavenTong
 * @date 2019/11/22 5:26 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stadium implements Serializable {
    private static final long serialVersionUID = -4454785377509579955L;

    private Integer stadiumId;

    @NotNull
    private String stadiumName;

    private Integer type;
    private String location;

    @Size(max = 200, message = "场馆描述最多200字")
    private String description;
    private BigDecimal price;
    private String picture;

    private List<Booking> bookingList;
}
