package cn.sibetech.core.util.excel;

import lombok.Data;

import java.util.List;

/**
 * @author liwj
 * @date 2022/3/17 13:45
 */
@Data
public class ExcelReadData {
    private List<String> heads;
    private List<List<String>> data;

    public ExcelReadData(List<String> heads, List<List<String>> data) {
        this.heads = heads;
        this.data = data;
    }
}
