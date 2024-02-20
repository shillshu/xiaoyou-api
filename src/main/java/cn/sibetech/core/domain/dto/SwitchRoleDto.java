package cn.sibetech.core.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liwj
 * @date 2022/2/9 16:36
 */
@Data
@ApiModel
public class SwitchRoleDto {
    @NotBlank(message = "角色不能为空")
    @ApiModelProperty(value = "角色id", required = true)
    private String roleId;
//    @ApiModelProperty(value = "是否设置为默认登录角色，1:设置，0:不设置")
//    private String isDefault;
}
