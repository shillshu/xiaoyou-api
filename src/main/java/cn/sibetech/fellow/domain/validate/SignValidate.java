package cn.sibetech.fellow.domain.validate;

import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.Sign;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
public class SignValidate extends BaseValidate{
    public static List<String> validate(Sign dto, boolean validateNull) {
        List<String> errors = new ArrayList<>();
        validateString(errors, "签到名称", dto.getName(), null,60, validateNull);
        return errors;
    }
}
