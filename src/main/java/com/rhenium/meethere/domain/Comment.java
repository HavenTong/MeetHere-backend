package com.rhenium.meethere.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HavenTong
 * @date 2019/11/22 5:23 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    private static final long serialVersionUID = -8717397300631261545L;

    private Integer commentId;
    private Integer stadiumId;
    private Integer customerId;
    private String customerName;

    private LocalDateTime commentPostTime;
    private String commentContent;
    private Integer likes;

}
