package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_activity_mark")
public class ActivityMark extends BaseModel{
    @TableField
    private String itemId;

    @TableField
    private String fellowId;

    @TableField
    private Float latitude;

    @TableField
    private Float longitude;

    @TableField
    private int orderNo;

    @TableField
    private String qdsj;
}
