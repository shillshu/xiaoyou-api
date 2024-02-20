package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_vip_kind")
public class VipKind extends BaseModel {
    @TableField
    private String mc;

    @TableField
    private String ownerType;

    @TableField
    private String owner;

    @TableField
    private String ownerName;

    @TableField
    private int orderno;

}
