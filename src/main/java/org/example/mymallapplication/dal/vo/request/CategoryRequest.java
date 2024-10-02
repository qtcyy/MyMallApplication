package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author chengyiyang
 */
@Data
public class CategoryRequest {
    @NotBlank(message = "name不能为空")
    private String name;
    @NotBlank(message = "description不能为空")
    private String description;

}
