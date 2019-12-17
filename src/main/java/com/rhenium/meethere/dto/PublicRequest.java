package com.rhenium.meethere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HavenTong
 * @date 2019/12/16 5:52 下午
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PublicRequest {
    private Integer userId;
}
