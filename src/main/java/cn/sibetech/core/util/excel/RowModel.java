package cn.sibetech.core.util.excel;

import lombok.Getter;
import lombok.Setter;

/**
 * excel行模型
 * @author liwj
 * @date 2022/3/23 10:58
 */
@Getter
@Setter
public class RowModel {

    private boolean horizontal;
    private String name;
    private Object data;
}
