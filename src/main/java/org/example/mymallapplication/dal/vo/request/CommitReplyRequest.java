package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author chengyiyang
 */
@Data
public class CommitReplyRequest {
    @NotBlank(message = "父评论不能为空")
    private String parentId;
    @NotBlank(message = "内容不能为空")
    private String content;
}
