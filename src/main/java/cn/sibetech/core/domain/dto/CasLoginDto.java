package cn.sibetech.core.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CasLoginDto {
    @ApiModelProperty(value = "token")
    private String token;
    @ApiModelProperty(value = "用户姓名")
    private String name;
    @ApiModelProperty("工号")
    private String username;
    @ApiModelProperty("部门名称")
    private String deptName;
    @ApiModelProperty("用户标签")
    private String userGroup;
    @ApiModelProperty("用户标签名称")
    private String userGroupName;
    private String userFace;
    @ApiModelProperty("welink头像")
    private String avatar;
    @ApiModelProperty("VPN标识")
    private boolean vpn = false;
    public CasLoginDto() {
    }
}
