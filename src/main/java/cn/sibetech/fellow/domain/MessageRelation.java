package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_message_relation")
public class MessageRelation extends BaseModel {
    @TableField
    private String receiveId;

    @TableField
    private String sendId;
}
