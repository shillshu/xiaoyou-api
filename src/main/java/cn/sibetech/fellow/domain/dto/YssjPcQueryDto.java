package cn.sibetech.fellow.domain.dto;

import cn.sibetech.core.domain.dto.PagerDto;
import lombok.Data;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
@Data
public class YssjPcQueryDto extends PagerDto {
    /* 学年*/
    private String xn;
}
