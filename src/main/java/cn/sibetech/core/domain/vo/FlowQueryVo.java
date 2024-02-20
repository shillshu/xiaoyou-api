package cn.sibetech.core.domain.vo;

import cn.sibetech.core.domain.ShiroUser;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FlowQueryVo {
    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 当前页码
     */
    private int current;
    /**
     * 每页条数
     */
    private int size;

    private List<String> flowKey = new ArrayList<>();

    private ShiroUser currentUser;
}
