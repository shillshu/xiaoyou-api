package cn.sibetech.core.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel
public class SortModel {

    @ApiModelProperty("字段")
    private String name;
    @ApiModelProperty("排序，ascend（升序）descend（降序）")
    private String order;

}
