package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_fellow_bind")
public class FellowBind extends BaseModel {
    @TableField
    private String openId;

    @TableField
    private String fellowId;

}
