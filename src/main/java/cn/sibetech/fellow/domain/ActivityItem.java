package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_activity_item")
public class ActivityItem extends BaseModel {

    @TableField
    private String itemCode;

    @TableField
    private String itemName;

    @TableField
    private String valid;

    @TableField
    private String description;

    @TableField
    private String isZt;

    @TableField
    private float latitude; //'纬度，浮点数，范围为-90~90，负数表示南纬'

    @TableField
    private float longitude; //'经度，浮点数，范围为-180~180，负数表示西经'

    @TableField
    private int scope; //范围：m
}
