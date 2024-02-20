package cn.sibetech.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("p_class")
public class DeptClass {
    @TableId
    private String id;
    @TableField
    private String bh;
    @TableField
    private String bjmc;

    @TableField
    private String xyId;

    @TableField
    private String majorId;
    @TableField
    private String nj;
}
