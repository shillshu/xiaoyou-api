package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_fellow_branch")
public class FellowBranch extends BaseModel {
    @TableField
    private String name;

    @TableField
    private String englishName;

    @TableField
    private String foundDate;

    @TableField
    private String phone;

    @TableField
    private String email;

    @TableField
    private String owner;

    @TableField
    private String mainPage;

    @TableField
    private String workplace;

    @TableField
    private String address;

    @TableField
    private String introduction;

    @TableField
    private String continent;

    @TableField
    private String country;

    @TableField
    private String province;

    @TableField
    private String city;

    @TableField
    private String fatherId;

    @TableField
    private String sortId;

    @TableField
    private String xylxr;

    @TableField
    private String sfcbxykw;

    @TableField
    private String cbzq;

    @TableField
    private String lncghd;

    @TableField
    private String type;

}
