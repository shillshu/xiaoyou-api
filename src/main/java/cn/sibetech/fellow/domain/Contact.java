package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@TableName(value = "xy_contact")
public class Contact extends BaseModel{
    /** 校友ID */
    @TableField
    private String fellowId;

    /** 校友姓名 */
    @TableField
    private String xm;

    /** 联络日期 */
    @TableField
    private String llrq;

    /** 联络主题 */
    @TableField
    private String llzt;

    /** 日程安排 */
    @TableField
    private String rcap;

    /** 具体内容 */
    @TableField
    private String jtnr;

    /** 地点 */
    @TableField
    private String dd;

    /** 校方领导 */
    @TableField
    private String xfld;

    /** 参与人员 */
    @TableField
    private String cyry;

    /** 赠送礼品 */
    @TableField
    private String zslp;

    /** 备注 */
    @TableField
    private String bz;

    @TableField( exist = false)
    private List<ContactFellow> fellowList = new ArrayList<>();

}
