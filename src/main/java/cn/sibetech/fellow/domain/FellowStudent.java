package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/*
*
* 此表只能查询 作为同步数据中间表 也可改为视图
* */
@Data
@TableName(value = "xy_fellow_student")
public class FellowStudent extends BaseModel {
    @TableField
    private String xh;

    @TableField
    private String xm;

    @TableField
    private String xb;

    @TableField
    private String csrq;

    @TableField
    private String pycc;

    @TableField
    private String xyId;

    @TableField
    private String xy;

    @TableField
    private String zy;

    /*只要年份*/
    @TableField
    private String rxsj;

    /*只要年份*/
    @TableField
    private String lxsj;

    @TableField
    private String lxdh;

    @TableField
    private String qqh;

    @TableField
    private String email;

    @TableField
    private String zjhm;

    @TableField
    private String szd;

    @TableField
    private String zzmm;

    @TableField
    private String gzdw;

    @TableField
    private String gzdwszd;

    @TableField
    private String status;

}
