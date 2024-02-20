package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_fellow_address")
public class FellowAddress extends BaseModel {
    @TableField
    private String dataId;

    @TableField
    private String gb;

    @TableField
    private String sf;

    @TableField
    private String sx;

    @TableField
    private String xxdz;

    @TableField
    private String yzbm;

    @TableField
    private String isdefault;

}
