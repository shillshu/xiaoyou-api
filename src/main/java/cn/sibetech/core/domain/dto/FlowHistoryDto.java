package cn.sibetech.core.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FlowHistoryDto {
    private String activityId;
    private String activityName;
    private List<FlowHistoryItemDto> list = new ArrayList<>();
}
