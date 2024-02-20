package cn.sibetech.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author liwj
 * @create 2023/9/20
 */
@Getter
@Setter
public class FlowDefDto {

    private String id;
    private String name;
    private String flowKey;
    private String category;
    private int version;
    private String deploymentId;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date deploymentTime;

    private Integer suspensionState;

}
