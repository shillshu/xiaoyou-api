package cn.sibetech.core.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoleQueryDto extends PagerDto {
    @ApiModelProperty(value = "角色名称")
    private String name;
}
