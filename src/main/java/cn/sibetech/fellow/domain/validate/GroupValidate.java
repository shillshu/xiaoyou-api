package cn.sibetech.fellow.domain.validate;

import cn.sibetech.fellow.domain.Group;
import cn.sibetech.fellow.domain.VipKind;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
public class GroupValidate extends BaseValidate{
    public static List<String> validate(Group dto, boolean validateNull) {
        List<String> errors = new ArrayList<>();
        validateString(errors, "名称", dto.getName(), null,60, validateNull);
        return errors;
    }
}
