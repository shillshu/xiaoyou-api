package cn.sibetech.fellow.domain.dto;

import cn.sibetech.core.domain.dto.PagerDto;
import lombok.Data;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
@Data
public class DonationItemQueryDto extends PagerDto {
    private String owner;

    private String valid;

    private String ownerType;
}
