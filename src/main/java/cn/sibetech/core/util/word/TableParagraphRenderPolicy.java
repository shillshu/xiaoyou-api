package cn.sibetech.core.util.word;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import static com.deepoove.poi.policy.TextRenderPolicy.Helper.REGEX_LINE_CHARACTOR;

public class TableParagraphRenderPolicy implements RenderPolicy {

    public static final String SPACE = "    ";

    @Override
    public void render(ElementTemplate eleTemplate, Object renderData, XWPFTemplate template) {
        XWPFRun run = ((RunTemplate) eleTemplate).getRun();
        String data = null == renderData ? "" : (String) renderData;
        String[] split = data.split(REGEX_LINE_CHARACTOR, -1);
        if (split.length <= 1) {
            run.setText(SPACE + trim(split[0]), 0);
        } else {
            run.setText(SPACE + trim(split[0]), 0);
            for (int i = 1; i < split.length; i++) {
                run.addCarriageReturn();
                run.setText(SPACE + trim(split[i]));
            }
        }
    }

    public String trim(String text) {
        return text.trim();
    }

}
