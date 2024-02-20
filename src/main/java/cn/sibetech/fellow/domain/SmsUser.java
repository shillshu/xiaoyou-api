package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_sms_user")
public class SmsUser extends BaseModel{
    @TableField
    private String msgId;

    @TableField
    private String userId;

    @TableField
    private String status;

}
