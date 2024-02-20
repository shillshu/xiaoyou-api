package cn.sibetech.fellow.domain.dto;

import cn.sibetech.core.domain.dto.PagerDto;
import lombok.Data;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
@Data
public class FellowStudentQueryDto extends PagerDto {

    private String xh;

    private String xm;

    private String xb;

    private String xy;

    private String xyId;

    private String rxsj;

    private String lxsj;

    private String status;


}
