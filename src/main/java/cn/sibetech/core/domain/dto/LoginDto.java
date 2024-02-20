package cn.sibetech.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liwj
 * @date 2022/1/24 13:41
 */

@Data
@ApiModel
public class LoginDto {
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", required = true)
    @JsonProperty("u")
    private String username;
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true)
    @JsonProperty("p")
    private String password;
    @JsonProperty("id")
    private String id;
}
