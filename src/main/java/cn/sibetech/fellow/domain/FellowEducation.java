package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_fellow_education")
public class FellowEducation extends BaseModel {
    @TableField
    private String dataId;

    @TableField
    private String qssj;

    @TableField
    private String zzsj;

    @TableField
    private String xl;

    @TableField
    private String xh;

    @TableField
    private String xy;

    @TableField
    private String zy;

    @TableField
    private String bj;

    @TableField
    private String ds;

    @TableField
    private String status;

}
