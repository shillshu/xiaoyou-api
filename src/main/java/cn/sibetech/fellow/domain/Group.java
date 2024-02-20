package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "XY_GROUP")
public class Group extends BaseModel {
    @TableField
    private String name;

    @TableField
    private String cjr;

    @TableField
    private String bz;
}
