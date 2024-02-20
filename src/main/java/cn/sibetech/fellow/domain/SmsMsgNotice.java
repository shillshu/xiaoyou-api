package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_sms_msg_notice")
public class SmsMsgNotice extends BaseModel{
    @TableField
    private String userId;

    @TableField
    private String sendResult;

    @TableField
    private String sendParam;

}
