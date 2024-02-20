package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@TableName(value = "xy_fellow_vip_member")
public class FellowVipMember extends BaseModel {
    @TableField
    private String fellowId;

    @TableField
    private String kindId;

    @TableField
    private String flag;

    @TableField
    private String proposer;

    @TableField ( exist = false)
    private List<String> fellowIdList = new ArrayList<>();

}
