package cn.sibetech.core.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FlowCommentDto {
    private String type;

    private String comment;
}
