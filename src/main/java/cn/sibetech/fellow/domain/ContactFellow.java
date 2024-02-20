package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_contact_fellow")
public class ContactFellow extends BaseModel{
    /** 校友ID */
    @TableField
    private String fellowId;

    /** 校友姓名 */
    @TableField
    private String xm;

    /** 联络ID */
    @TableField
    private String contactId;

}
