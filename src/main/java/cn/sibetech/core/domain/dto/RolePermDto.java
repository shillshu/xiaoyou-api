package cn.sibetech.core.domain.dto;

import cn.sibetech.core.domain.PermModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class RolePermDto {

    @ApiModelProperty(value = "角色ID", required = true)
    private String roleId;
    @ApiModelProperty(value = "权限ID", required = true)
    private List<String> permString = new ArrayList<>();

    private List<PermModel> perms = new ArrayList<>();

}
