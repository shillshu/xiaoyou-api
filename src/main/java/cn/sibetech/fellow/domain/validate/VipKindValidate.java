package cn.sibetech.fellow.domain.validate;

import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.VipKind;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
public class VipKindValidate extends BaseValidate{
    public static List<String> validate(VipKind dto, boolean validateNull) {
        List<String> errors = new ArrayList<>();
        validateString(errors, "名称", dto.getMc(), null,60, validateNull);
        return errors;
    }
}
