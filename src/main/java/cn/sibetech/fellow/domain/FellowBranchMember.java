package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import liquibase.pro.packaged.S;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@TableName(value = "xy_fellow_branch_member")
public class FellowBranchMember extends BaseModel {
    @TableField
    private String branchId;

    @TableField
    private String fellowId;

    @TableField ( exist = false)
    private List<String> fellowIdList = new ArrayList<>();

    @TableField
    private String fellowGrade;

    @TableField
    private String director;

    @TableField
    private String status;

}
