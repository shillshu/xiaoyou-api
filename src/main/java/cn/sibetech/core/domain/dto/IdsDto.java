package cn.sibetech.core.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 公共VO，提供POST请求id数组参数时接口使用
 * @author kevin.w
 */
@Data
public class IdsDto {
    @ApiModelProperty(value = "主键ids", required = true)
    private List<String> ids;
}
