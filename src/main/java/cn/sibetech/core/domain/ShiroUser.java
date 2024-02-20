package cn.sibetech.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/21 14:22
 */
@Data
public class ShiroUser {
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("部门id")
    private String deptId;
    @ApiModelProperty("部门名称")
    private String deptName;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("token")
    private String token;
    @ApiModelProperty("用户类型")
    private String usertype;

    @ApiModelProperty("当前角色")
    private Role currentRole;
    @ApiModelProperty("当前用户所属权限")
    private List<String> permissions = new ArrayList<>();
    private String userKey;
    private Long loginTime;
    private Long expireTime;

    private List<Role> roles;

    private List<FlowGroup> flowGroups;

    private List<String> xyScopes = new ArrayList<>();

    private List<String> njScopes = new ArrayList<>();

    private List<String> bjScopes = new ArrayList<>();
    public ShiroUser() {
    }
    public ShiroUser(String id, String loginName, String name, String usertype) {
        this.userId = id;
        this.username = loginName;
        this.name = name;
        this.usertype = usertype;
//        this.deptName = deptName;
    }
}
