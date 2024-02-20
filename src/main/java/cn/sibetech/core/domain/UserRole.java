package cn.sibetech.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
@Data
@TableName("p_user_role")
public class UserRole implements Serializable {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    @TableField
    private String userId;
    @TableField
    private String roleId;

    @TableField( exist = false)
    private String scope;
}
