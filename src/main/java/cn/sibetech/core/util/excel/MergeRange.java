package cn.sibetech.core.util.excel;

import lombok.Data;

/**
 * @author liwj
 * @date 2022/3/22 14:34
 */
@Data
public class MergeRange {
    private String lastValue;
    private int startRow;
    private int endRow;
    private int startCell;
    private int endCell;

    public MergeRange(String lastValue, int startRow, int endRow, int startCell, int endCell) {
        this.startRow = startRow;
        this.endRow = endRow;
        this.startCell = startCell;
        this.endCell = endCell;
        this.lastValue = lastValue;
    }
}
