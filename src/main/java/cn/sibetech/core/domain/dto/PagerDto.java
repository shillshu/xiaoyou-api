package cn.sibetech.core.domain.dto;

import cn.sibetech.core.request.PageAction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/26 14:33
 */
@Data
public class PagerDto implements Serializable {
    @NotNull(message = "页码不能为空", groups = {PageAction.class})
    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum;
    @NotNull(message = "每页记录数不能为空", groups = {PageAction.class})
    @ApiModelProperty(value = "每页记录数", required = true)
    private Integer pageSize;
    @ApiModelProperty("排序")
    private List<SortModel> sort = new ArrayList<>();
}
