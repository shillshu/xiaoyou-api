package cn.sibetech.core.util.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author liwj
 * @date 2022/3/22 14:33
 */
public class MergeStrategy extends AbstractMergeStrategy {
    // 合并的列编号，从0开始，指定的index或自己按字段顺序数
    private Set<Integer> mergeCellIndex = new HashSet<>();

    // 数据集大小，用于区别结束行位置
    private Integer maxRow = 0;

    // 禁止无参声明
    private MergeStrategy() {
    }

    public MergeStrategy(Integer maxRow, int... mergeCellIndex) {
        Arrays.stream(mergeCellIndex).forEach(item -> {
            this.mergeCellIndex.add(item);
        });
        this.maxRow = maxRow;
    }

    // 记录上一次合并的信息
    private Map<Integer, MergeRange> lastRow = new HashedMap();

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        int currentCellIndex = cell.getColumnIndex();
        // 判断该行是否需要合并
        if (mergeCellIndex.contains(currentCellIndex)) {
            String currentCellValue = cell.getStringCellValue();
            int currentRowIndex = cell.getRowIndex();
            if (!lastRow.containsKey(currentCellIndex)) {
                // 记录首行起始位置
                lastRow.put(currentCellIndex, new MergeRange(currentCellValue, currentRowIndex, currentRowIndex, currentCellIndex, currentCellIndex));
                return;
            }
            //有上行这列的值了，拿来对比.
            MergeRange mergeRange = lastRow.get(currentCellIndex);
            if (!(mergeRange.getLastValue() != null && mergeRange.getLastValue().equals(currentCellValue))) {
                // 结束的位置触发下合并.
                // 同行同列不能合并，会抛异常
                if (mergeRange.getStartRow() != mergeRange.getEndRow() || mergeRange.getStartCell() != mergeRange.getEndCell()) {
                    sheet.addMergedRegionUnsafe(new CellRangeAddress(mergeRange.getStartRow(), mergeRange.getEndRow(), mergeRange.getStartCell(), mergeRange.getEndCell()));
                }
                // 更新当前列起始位置
                lastRow.put(currentCellIndex, new MergeRange(currentCellValue, currentRowIndex, currentRowIndex, currentCellIndex, currentCellIndex));
            }
            // 合并行 + 1
            mergeRange.setEndRow(mergeRange.getEndRow()+1);
            // 结束的位置触发下最后一次没完成的合并
            if (relativeRowIndex!=null && relativeRowIndex.equals(maxRow - 1)) {
                MergeRange lastMergeRange = lastRow.get(currentCellIndex);

                // 同行同列不能合并，会抛异常
                if (lastMergeRange.getStartRow() != lastMergeRange.getEndRow() || lastMergeRange.getStartCell() != lastMergeRange.getEndCell()) {
                    sheet.addMergedRegionUnsafe(new CellRangeAddress(lastMergeRange.getStartRow(), lastMergeRange.getEndRow(), lastMergeRange.getStartCell(), lastMergeRange.getEndCell()));
                }
            }
        }
    }
}
