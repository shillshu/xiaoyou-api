package cn.sibetech.fellow.domain.dto;

import cn.sibetech.core.domain.dto.PagerDto;
import lombok.Data;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
@Data
public class SignFellowQueryDto extends PagerDto {
    private String type; /* 1 计划内  2 计划外  3 未签到*/

    private String fellowId;

    private String pcId;

    private String deptId;

    private String xy;

    private String jhStatus;

}
