package cn.sibetech.core.domain.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liwj
 * @create 2023/11/1
 */
@Getter
@Setter
public class ExportDto {
    private boolean flag;

    private String errmsg;

    private String filename;

    private String filepath;
}
