package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@TableName(value = "xy_message_detail")
public class MessageDetail extends BaseModel {
    @TableField
    private String relationId;

    @TableField
    private String content;

    @TableField
    private String fellowId;

    @TableField
    private String parentId;

    @TableField ( exist = false)
    private String xm;

    @TableField ( exist = false)
    private String receiveId;

    @TableField ( exist = false)
    private String sendId;

    @TableField(
            exist = false
    )
    private List<MessageDetail> sonList = new ArrayList();;
}
