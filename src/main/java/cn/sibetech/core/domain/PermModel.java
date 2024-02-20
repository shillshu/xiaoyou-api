package cn.sibetech.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/2/14 16:24
 */
@Data
public class PermModel {
    @ApiModelProperty(value = "权限名称")
    private String name;
    @ApiModelProperty(value = "权限值")
    private String value;
    @ApiModelProperty(value = "子权限")
    private List<PermModel> subPerms = new ArrayList<>();

    private boolean checked;
}
