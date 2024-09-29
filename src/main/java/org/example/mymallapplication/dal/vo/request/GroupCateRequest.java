package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class GroupCateRequest {
    @NotBlank(message = "类别ID不能为空")
    String cateId;
    @NotBlank(message = "产品ID不能为空")
    List<String> productIds;
}
