package cn.sibetech.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
@Data
@TableName("p_role")
public class Role extends BaseModel {
    @TableField
    private String name;
    @TableField
    private String scope;
}
