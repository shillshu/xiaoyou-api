package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;



@Data
@TableName(value = "xy_activity_fellow")
public class ActivityFellow extends BaseModel {
    @TableField
    private String fellowId;

    @TableField(exist=false)
    private String fellowXm;

    @TableField
    private String activityId;

    @TableField
    private String ddsj;

    @TableField
    private String ddtime;

    @TableField
    private String ddfs;

    @TableField
    private String dddd;

    @TableField
    private String sfxyjz;

    @TableField
    private String sfxyydfj;

    @TableField
    private String lxfs;

    @TableField
    private int txrs;

    @TableField
    private String remark;

    @TableField
    private String isValid;

}
