package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "xy_config")
public class XyConfig extends BaseModel {
    @TableField
    private String key;
    @TableField
    private String value;
    @TableField
    private String type;
    @TableField
    private String remark;
}
