package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;


@Data
@TableName(value = "xy_donation")
public class Donation extends BaseModel{
    @TableField
    private String fellowId;

    @TableField
    private String itemId;

     @TableField
    private String jzsj;  //'捐赠时间'

     @TableField
    private String jzfs; //'捐赠方式：个人/集体等'

     @TableField
    private String jzlx; //'捐赠类型：实物/货币等

     @TableField
    private BigDecimal jzje; //捐赠金额

     @TableField
    private String jzyt; //'捐赠用途'

     @TableField
    private String yjrq; //'邮寄日期'

     @TableField
    private String sfkdxm; //'是否刊登姓名'

     @TableField
    private String bz; //'备注'

     @TableField
    private String jzly; //'捐赠来源'

     @TableField
    private String nd; //'年度'

     @TableField
    private String hblx; //'货币类型'

     @TableField
    private String sfjrq; //'收发据日期'

     @TableField
    private String jzrxz; //'捐赠人性质'

     @TableField
    private String relationId;

     @TableField
    private String entername; //'名'

     @TableField
    private String jzsw; //'捐赠实物'

     @TableField
    private String sfpg; // '是否评估'

     @TableField
    private String openId; //'微信id'

     @TableField
    private String xm;

    @TableField(exist = false)
    private String jzrxm;

     @TableField
    private String xy;

     @TableField
    private String tel;

     @TableField
    private String email;

     @TableField
    private String orderNo;

     @TableField
    private String status;

     @TableField
    private String wxzp;

    @TableField
    private String zsPath;

    @TableField
    private String jzxyId;

    @TableField
    private String jzxy;

    @TableField
    private String zy;

    @TableField
    private String zyId;

    @TableField
    private String rxnf;

    @TableField
    private String code;

    @TableField
    private Integer zsNumber;

    @TableField
    private String sfxyfp;

    @TableField
    private String fptt;
}
