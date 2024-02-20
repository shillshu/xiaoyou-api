package cn.sibetech.fellow.domain.validate;

import cn.sibetech.fellow.domain.Activity;
import cn.sibetech.fellow.domain.Fellow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
public class FellowValidate extends BaseValidate{
    public static List<String> validate(Fellow dto, boolean validateNull) {
        List<String> errors = new ArrayList<>();
        validateString(errors, "姓名", dto.getXm(), null,60, validateNull);
        return errors;
    }
}
