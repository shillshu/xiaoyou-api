package cn.sibetech.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("p_user")
public class User extends BaseModel {
    @TableField
    private String username;
    @TableField
    private String password;
    @TableField
    private String truename;

    @TableField
    private String userNumber;
    @TableField
    private String salt;
    @TableField
    private String usertype;
    @TableField
    private String deptId;
    @TableField(exist = false)
    private String deptName;
    @TableField
    private String sex;
    @TableField
    private String status;
    @TableField
    private String email;
    @TableField
    private String mobile;
    @TableField
    private String address;

    @TableField ( exist = false)
    private String roleId;

    @TableField ( exist = false)
    private String roleName;
}
