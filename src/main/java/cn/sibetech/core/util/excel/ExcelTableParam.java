package cn.sibetech.core.util.excel;

import com.alibaba.excel.write.handler.CellWriteHandler;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/3/23 15:26
 */
@Data
public class ExcelTableParam {

    private String filePath;
    /**
     * 导出文件名
     */
    private String filename;

    private List<List<String>> heads = new ArrayList<>();
    /**
     * 填充数据
     */
    private List<List<Object>> data = new ArrayList<>();

    private MergeSameRowsStrategy mergeSameRowsStrategy;
    /**
     * 响应
     */
    private HttpServletResponse response;

    private CellWriteHandler cellWriteHandler;
    /**
     * 检查不允许为空的属性
     */
    public boolean isValid() {
        return ObjectUtils.allNotNull(filename, heads, data, response);
    }
}
