package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@TableName(value = "xy_donation_item")
public class DonationItem extends BaseModel{

     @TableField
    private String code;

     @TableField
    private String name;

     @TableField
    private String valid; //'有效/无效'

     @TableField
    private String description; //'项目详情'

     @TableField
    private String ownerType; //'项目所有者(global：校级，dept：院系级，branch：分会级)'

     @TableField
    private String owner; //

    @TableField( exist = false)
    private String ownerName; //

     @TableField
    private String jzmb; //'捐赠目标'

     @TableField
    private String cjr; //'创建人'

     @TableField
    private String lxr; //'联系人'

     @TableField
    private String tel; //'电话'

     @TableField
    private String email; //'邮箱'

     @TableField
    private String zp; //照片地址

    @TableField
    private String zs;/*证书地址*/

    @TableField
    private String je;/*金额*/

    @TableField(exist = false)
    private List jeList = new ArrayList<>();

    @TableField(exist = false)
    private BigDecimal jzje;

    @TableField(exist = false)
    private int rs;

}
