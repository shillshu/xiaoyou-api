package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_activity")
public class Activity extends BaseModel {
    @TableField
    private String name;

    @TableField
    private String itemId;

    @TableField(exist = false)
    private String itemName;

    @TableField(exist = false)
    private int cyrs;

    @TableField
    private String startTime;

    @TableField
    private String place;

    @TableField
    private String endTime;

    @TableField
    private String fellowCount;

    @TableField
    private String jfly;

    @TableField
    private String cjxld;

    @TableField
    private String lp;

    @TableField
    private String jfje;

    @TableField
    private String bmStartTime;

    @TableField
    private String bmEndTime;

    @TableField
    private String cjr;

    @TableField
    private String lxr;

    @TableField
    private String lxrdh;

    @TableField
    private String sfsysfjz;

    @TableField
    private String addUserId;

    @TableField
    private String updateUserId;

    @TableField
    private String deptId;

    @TableField
    private String type;

    @TableField
    private String remark;

    @TableField(exist = false)
    private int bmFlag;

    @TableField(exist = false)
    private String isZt;

    @TableField(exist = false)
    private String isValid;

    /* 是否可报名*/
    @TableField(exist = false)
    private String state;

    @TableField(exist = false)
    private ActivityFellow activityFellow = new ActivityFellow();


}
