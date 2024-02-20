package cn.sibetech.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("p_dept")
public class BizField extends BaseModel {
    @TableField
    private String bizId;
    @TableField
    private String fieldid;
    @TableField
    private String fielddesc;
    @TableField
    private String orderno;
    @TableField
    private String isDisplay;
    @TableField
    private String isExport;
    @TableField
    private String displayWidth;
    @TableField
    private String sfgd;
}
