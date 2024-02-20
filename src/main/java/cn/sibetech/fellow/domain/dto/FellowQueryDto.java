package cn.sibetech.fellow.domain.dto;

import cn.sibetech.core.domain.dto.PagerDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
@Data
public class FellowQueryDto extends PagerDto {

    private String xm;

    private String xb;

    private String xy;

    /* 校友分会*/
    private String branchId;

    /* 重点校友分类*/
    private String kindId;


}
