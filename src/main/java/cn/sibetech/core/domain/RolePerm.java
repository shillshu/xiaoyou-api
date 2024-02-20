package cn.sibetech.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
@Data
@TableName("p_role_perm")
public class RolePerm implements Serializable {
    @TableField
    private String roleId;
    @TableField
    private String permString;

    @TableField
    private String serviceId;
}
