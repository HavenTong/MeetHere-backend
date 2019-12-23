package com.rhenium.meethere.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @NotNull
    private Integer stadiumId;

    @NotNull
    private Integer customerId;

    private LocalDateTime commentPostTime;

    @Size(max = 200, message = "评论内容最多200字")
    private String commentContent;
    private Integer likes;

    // 关联用户
    private Customer customer;
    private Stadium stadium;

}
