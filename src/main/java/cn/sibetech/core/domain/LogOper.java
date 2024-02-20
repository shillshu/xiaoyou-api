package cn.sibetech.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author kevin.w
 */
@Data
@TableName("p_log_oper")
public class LogOper extends BaseModel {
    @TableField
    private String username;
    @TableField
    private String content;
    @TableField
    private String clientIp;
    @TableField
    private String clientSystem;
    @TableField
    private String clientBrowser;

}
