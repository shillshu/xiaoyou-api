package cn.sibetech.fellow.domain.dto;

import cn.sibetech.core.domain.dto.PagerDto;
import lombok.Data;

import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
@Data
public class ActivityFellowQueryDto extends PagerDto {
    /* 学年*/
    private String activityId;

    private String sfxyydfj;

    private String sfxyjz;

    private String fellowXm;

    private String isValid;
}
