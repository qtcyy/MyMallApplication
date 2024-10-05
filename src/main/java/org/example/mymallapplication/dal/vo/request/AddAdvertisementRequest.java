package org.example.mymallapplication.dal.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.mymallapplication.dal.enums.AdStatus;

import java.time.LocalDateTime;

/**
 * @author chengyiyang
 */
@Data
public class AddAdvertisementRequest {
    @NotBlank(message = "广告标题不能为空")
    private String title;
    @NotBlank(message = "广告图片不能为空")
    private String imageUrl;
    @NotBlank(message = "广告链接不能为空")
    private String redirectUrl;
    @NotBlank(message = "广告开始时间不能为空")
    private LocalDateTime startTime;
    @NotBlank(message = "广告结束时间不能为空")
    private LocalDateTime endTime;
    @NotBlank(message = "广告优先级不能为空")
    private Integer priority;
    @NotBlank(message = "广告状态不能为空")
    private AdStatus status;
}
