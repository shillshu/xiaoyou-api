package cn.sibetech.core.domain;

import cn.sibetech.core.domain.ShiroUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "登录返回")
public class LoginVo {
    @ApiModelProperty(value = "是否需要切换角色")
    private boolean chooseRole;
    @ApiModelProperty(value = "用户信息")
    private ShiroUser user;

    public LoginVo() {
    }

    public LoginVo(boolean chooseRole, ShiroUser user) {
        this.chooseRole = chooseRole;
        this.user = user;
    }
}
