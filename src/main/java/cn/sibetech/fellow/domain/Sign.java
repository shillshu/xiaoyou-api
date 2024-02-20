package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;



@Data
@TableName(value = "xy_sign_pc")
public class Sign extends BaseModel{
    @TableField
    private String name;

    @TableField
    private String activityId;

    @TableField(exist = false )
    private String activityName;

    @TableField
    private int jhqdrs;

    @TableField
    private int yqdrs;

    @TableField(exist = false )
    private int dqdrs;

    @TableField(exist = false )
    private int jhwrs;

    @TableField
    private String status;

    @TableField
    private String sffpxy;

    @TableField
    private String sfglhd;
}
