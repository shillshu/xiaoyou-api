package cn.sibetech.core.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @create 2023/9/22
 */
@Getter
@Setter
public class FlowActivityVo {
    private double x;
    private double y;
    private double width;
    private double height;

    private String name;
    private List<FlowActivityFlowVo> flows = new ArrayList<>();
    private boolean show;
}
