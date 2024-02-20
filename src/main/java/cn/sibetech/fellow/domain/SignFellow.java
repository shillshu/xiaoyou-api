package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_sign_fellow")
public class SignFellow extends BaseModel {


    @TableField(exist = false)
    private String qdName;

    @TableField
    private String pcId;

    @TableField
    private String fellowId;

    @TableField
    private String deptId;

    @TableField(exist = false)
    private String qddd;

    @TableField(exist = false)
    private String xm;

    @TableField(exist = false)
    private String xb;

    @TableField(exist = false)
    private String sj;

    @TableField(exist = false)
    private String qq;

    @TableField(exist = false)
    private String xy;

    @TableField(exist = false)
    private String rxnf;

    @TableField
    private String status;

    @TableField
    private String jhStatus;
}
