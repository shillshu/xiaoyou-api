package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_branch_fellow_grade")
public class FellowBranchGrade extends BaseModel {
    @TableField
    private String branchMemberId;

    @TableField
    private String grade;
}
