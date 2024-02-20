package cn.sibetech.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("p_dept")
public class Dept {
    @TableId
    private String id;
    @TableField
    private String name;
    @TableField
    private String code;
    @TableField
    private String flag;
}
