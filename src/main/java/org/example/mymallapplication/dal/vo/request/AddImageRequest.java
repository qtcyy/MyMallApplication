package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author chengyiyang
 */
@Data
public class AddImageRequest {
    @NotBlank(message = "评论id不能为空")
    private String reviewId;
    @NotBlank(message = "图片ID不能为空")
    private String imageUrl;
}
