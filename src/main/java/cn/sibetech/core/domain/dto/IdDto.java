package cn.sibetech.core.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 公共VO，提供POST请求只有单个id参数时接口使用
 * @author kevin.w
 */
@Data
public class IdDto {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty(value = "id", required = true)
    private String id;
}
