package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_fellow_resume")
public class FellowResume extends BaseModel {
    @TableField
    private String dataId;

    @TableField
    private String qssj;

    @TableField
    private String zzsj;

    @TableField
    private String gzdw;

    @TableField
    private String zw;

    @TableField
    private String hy;

    @TableField
    private String gznr;

    @TableField
    private String jzcs;

    @TableField
    private String zc;

    @TableField
    private String status;

}
