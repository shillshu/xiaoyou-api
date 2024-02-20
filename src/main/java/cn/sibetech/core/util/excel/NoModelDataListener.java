package cn.sibetech.core.util.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.util.ListUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author liwj
 * @date 2022/3/17 11:17
 */
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {

    public static int BATCH_COUNT = 100;
    private final Consumer<ExcelReadData> consumer;
    private ExcelReadData excelData = new ExcelReadData(ListUtils.newArrayListWithExpectedSize(10), ListUtils.newArrayListWithExpectedSize(BATCH_COUNT));

    public NoModelDataListener(Consumer<ExcelReadData> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        headMap.keySet().forEach(key -> {
            ReadCellData item = headMap.get(key);
            excelData.getHeads().add(item.getStringValue());
        });
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        List<String> lineData = new ArrayList<>(excelData.getHeads().size());
        for(int i=0; i<excelData.getHeads().size();i++) {
            lineData.add(data.get(i));
        }
        excelData.getData().add(lineData);
        if (excelData.getData().size() >= BATCH_COUNT) {
            consumer.accept(excelData);
            excelData.setData(ListUtils.newArrayListWithExpectedSize(BATCH_COUNT));
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtils.isNotEmpty(excelData.getData())) {
            consumer.accept(excelData);
        }
    }
}
