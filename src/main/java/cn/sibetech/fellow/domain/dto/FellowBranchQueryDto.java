package cn.sibetech.fellow.domain.dto;

import cn.sibetech.core.domain.dto.PagerDto;
import lombok.Data;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
@Data
public class FellowBranchQueryDto extends PagerDto {

    private String fatherId;

    private String name;

    private String owner;

    private String type;


}
