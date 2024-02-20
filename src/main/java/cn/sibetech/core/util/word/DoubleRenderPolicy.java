package cn.sibetech.core.util.word;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.math.BigDecimal;

public class DoubleRenderPolicy implements RenderPolicy {
    @Override
    public void render(ElementTemplate eleTemplate, Object data, XWPFTemplate template) {
        XWPFRun run = ((RunTemplate) eleTemplate).getRun();
        if(data instanceof Double || data instanceof Float) {
            run.setText(new BigDecimal(String.valueOf(data)).stripTrailingZeros().toPlainString(), 0);
        }
        else {
            run.setText("", 0);
        }
    }
}
