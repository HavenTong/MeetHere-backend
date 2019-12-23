package com.rhenium.meethere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/19 19:21
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
    private Integer commentId;
    private Integer stadiumId;
    private Integer customerId;

    @Size(max = 200, message = "评论内容最多200字")
    private String commentContent;
}
