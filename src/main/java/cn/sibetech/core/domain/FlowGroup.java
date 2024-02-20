package cn.sibetech.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liwj
 * @create 2023/9/26
 */
@Getter
@Setter
@TableName("p_flow_group")
public class FlowGroup extends BaseModel {
    @TableField
    private String name;
    @TableField
    private String status;
    @TableField
    private Integer orderNumber;
}
