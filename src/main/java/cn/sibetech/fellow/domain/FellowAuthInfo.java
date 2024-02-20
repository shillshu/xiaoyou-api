package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
@Data
@TableName("xy_fellow_authinfo")
public class FellowAuthInfo extends BaseModel {
    /* */
    @TableField
    private String bindId;

    @TableField
    private String fellowId;

    @TableField
    private String xm;

    @TableField
    private String rxnf;

    @TableField
    private String bynf;

    @TableField
    private String xh;

    @TableField
    private String xb;

    @TableField
    private String xy;

    @TableField(exist = false)
    private String xyName;

    @TableField
    private String xyId;

    @TableField(exist = false)
    private String[] xyIds;

    @TableField(exist = false)
    private String[] zts;

    @TableField
    private String zy;

    @TableField
    private String sj;

    @TableField
    private String code;

    @TableField
    private String qq;

    @TableField
    private String email;

    @TableField
    private String hy;

    @TableField
    private String gj;

    @TableField
    private String szd;

    @TableField
    private String szds;

    @TableField
    private String xl;

    @TableField
    private String csrq;

    @TableField
    private String zjlx;

    @TableField
    private String zjhm;

    @TableField
    private String bz;

    @TableField
    private String zzmm;

    @TableField
    private String zt;

    @TableField
    private String  xyfh;

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

    @TableField
    private String gzdw;

    @TableField
    private String xylx;

    @TableField
    private String shbz;

    @TableField
    private String ppfellowId;

    @TableField
    private String shrId;

    @TableField
    private String shsj;

    @TableField
    private String filePath;

    @TableField(exist = false)
    private String status;


}
