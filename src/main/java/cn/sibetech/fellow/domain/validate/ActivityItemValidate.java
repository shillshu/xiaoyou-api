package cn.sibetech.fellow.domain.validate;

import cn.sibetech.fellow.domain.Activity;
import cn.sibetech.fellow.domain.ActivityItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
public class ActivityItemValidate extends BaseValidate{
    public static List<String> validate(ActivityItem dto, boolean validateNull) {
        List<String> errors = new ArrayList<>();
        validateString(errors, "类型编码", dto.getItemCode(), null,20, validateNull);
        validateString(errors, "类别名称", dto.getItemName(), null,60, validateNull);
        return errors;
    }
}
