package cn.sibetech.core.util.excel;

import lombok.Data;

/**
 * excel列模型
 * @author liwj
 * @date 2022/3/23 15:24
 */
@Data
public class HeadModel {

    private String name;
    private String desc;
    public HeadModel() {
    }

    public HeadModel(String name) {
        this.name = name;
    }
}
