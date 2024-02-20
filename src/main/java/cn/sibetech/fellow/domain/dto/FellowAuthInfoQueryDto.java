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
public class FellowAuthInfoQueryDto extends PagerDto {
    /* 学院查询条件*/
    private List xyIds = new ArrayList<>();

    /* 状态查询条件*/
    private List zts = new ArrayList<>();

    /* 分组查询条件 group*/
    private List<String> groupIdList;

    private String xm;

    private String xb;

    private String xy;

    private String xyName;

    private String rxnf;

    private String xylx;

}
