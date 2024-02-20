package cn.sibetech.core.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserQueryDto extends PagerDto {
    private String truename;

    private String username;

    private String deptId;
    private List<String> deptIdList;
    private String usertype;
    private String status;
}
