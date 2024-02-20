package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_branch_sort")
public class BranchSort extends BaseModel {
    @TableField
    private String name;

    @TableField
    private String fatherId;

    @TableField
    private int orderNumber;

    @TableField
    private int branchCount;
}
