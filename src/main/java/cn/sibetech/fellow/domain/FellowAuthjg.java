package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@TableName(value = "xy_fellow_authjg")
public class FellowAuthjg extends BaseModel {
    @TableField( exist = false)
    private FellowAuthInfo info = new FellowAuthInfo();

    @TableField( exist = false)
    private String fellowId;
    @TableField
    private String infoId;

    @TableField
    private String spzt;

    @TableField
    private String spjg;

    @TableField
    private String spyj;

    @TableField
    private String spsj;

    @TableField
    private String bz;

    @TableField
    private String xyfh;

    @TableField
    private String xyfh1;

    @TableField
    private String dffh;

    @TableField
    private String dffh1;

    @TableField
    private String hyfh;

    @TableField
    private String hyfh1;
    
}
