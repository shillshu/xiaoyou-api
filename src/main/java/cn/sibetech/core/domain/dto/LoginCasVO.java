package cn.sibetech.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liwj
 * @date 2021/4/6 9:06
 */
@Data
@ApiModel
public class LoginCasVO {
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", required = true)
    @JsonProperty("u")
    private String username;


}
