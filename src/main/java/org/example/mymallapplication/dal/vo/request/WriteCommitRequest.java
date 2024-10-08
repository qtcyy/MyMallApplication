package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author chengyiyang
 */
@Data
public class WriteCommitRequest {
    @NotBlank(message = "商品ID不能为空")
    private String productId;

    @NotBlank(message = "打分不能为空")
    private Integer rating;

    private String content;
}
