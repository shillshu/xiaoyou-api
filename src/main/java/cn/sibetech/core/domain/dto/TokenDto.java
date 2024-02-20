package cn.sibetech.core.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liwj
 * @date 2022/1/24 17:00
 */
@Data
@ApiModel
public class TokenDto {
    @NotBlank(message = "token不能为空")
    @ApiModelProperty(value = "token", required = true)
    private String token;
}
