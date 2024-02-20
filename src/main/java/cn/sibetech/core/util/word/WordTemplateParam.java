package cn.sibetech.core.util.word;

import org.apache.commons.lang3.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liwj
 * @date 2022/3/23 10:56
 */
public class WordTemplateParam {
    /**
     * 模版
     */
    private String templatePath;
    /**
     * 导出文件名
     */
    private String filename;

    private String path;
//    /**
//     * 是否需要合并
//     */
//    private boolean merge;
//    /**
//     * 合并数据集大小，用于区别结束行位置
//     */
//    private Integer maxRow;
//    /**
//     * 合并的列编号
//     */
//    private Integer mergeCellIndex;

    /**
     * 填充数据
     */
    private Map<String, Object> data;

    private List<WordRenderPolicy> renderPolicies = new ArrayList<>();

    /**
     * 响应
     */
    private HttpServletResponse response;


    /**
     * 检查不允许为空的属性
     */
    public boolean isValid() {
        return ObjectUtils.allNotNull(templatePath, filename, data, response);
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<WordRenderPolicy> getRenderPolicies() {
        return renderPolicies;
    }

    public void setRenderPolicies(List<WordRenderPolicy> renderPolicies) {
        this.renderPolicies = renderPolicies;
    }
}
