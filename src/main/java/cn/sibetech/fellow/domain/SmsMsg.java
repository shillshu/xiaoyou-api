package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_sms_msg")
public class SmsMsg extends BaseModel{
    @TableField
    private String content;

    @TableField
    private String senderId;

    @TableField
    private String info;

    @TableField
    private String typeCode;

    @TableField( exist = false )
    private String count;

    @TableField( exist = false )
    private String ksrq;

    @TableField( exist = false )
    private String jsrq;

}
