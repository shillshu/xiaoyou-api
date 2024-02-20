package cn.sibetech.fellow.domain.validate;


import cn.sibetech.fellow.domain.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
public class ContactValidate extends BaseValidate{
    public static List<String> validate(Contact dto, boolean validateNull) {
        List<String> errors = new ArrayList<>();
        validateString(errors, "日期", dto.getLlrq(), null,20, validateNull);
        validateString(errors, "主题", dto.getLlzt(), null,60, validateNull);
        validateString(errors, "地点", dto.getDd(), null,60, validateNull);
        validateString(errors, "学校领导", dto.getXfld(), null,60, validateNull);
        validateString(errors, "日程安排", dto.getRcap(), null,60, validateNull);
        validateString(errors, "具体内容", dto.getJtnr(), null,60, validateNull);
        validateString(errors, "参与人员", dto.getCyry(), null,60, validateNull);
        validateString(errors, "赠送礼品", dto.getZslp(), null,60, validateNull);
        validateString(errors, "备注", dto.getBz(), null,60, validateNull);

        return errors;
    }
}
