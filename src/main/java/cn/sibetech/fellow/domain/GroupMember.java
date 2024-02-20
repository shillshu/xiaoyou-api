package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@TableName(value = "XY_GROUP_MEMBER")
public class GroupMember extends BaseModel {
    @TableField
    private String fellowId;

    @TableField
    private String groupId;

    @TableField
    private List<String> fellowIdList = new ArrayList<>();
}
