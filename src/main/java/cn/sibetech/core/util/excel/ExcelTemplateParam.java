package cn.sibetech.core.util.excel;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liwj
 * @date 2022/3/23 10:56
 */
@Data
public class ExcelTemplateParam {
    /**
     * 模版
     */
    private String templatePath;

    private String filePath;
    /**
     * 导出文件名
     */
    private String filename;
    /**
     * 是否需要合并
     */
    private boolean merge;
    /**
     * 合并数据集大小，用于区别结束行位置
     */
    private Integer maxRow;
    /**
     * 合并的列编号
     */
    private Integer mergeCellIndex;

    /**
     * 填充数据
     */
    private List<RowModel> data;

    /**
     * 响应
     */
    private HttpServletResponse response;

    /**
     * 检查不允许为空的属性
     */
    public boolean isValid() {
        return ObjectUtils.allNotNull(templatePath, filename, response);
    }
}
