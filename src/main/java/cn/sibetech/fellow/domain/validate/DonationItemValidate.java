package cn.sibetech.fellow.domain.validate;

import cn.sibetech.fellow.domain.ActivityItem;
import cn.sibetech.fellow.domain.DonationItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
public class DonationItemValidate extends BaseValidate{
    public static List<String> validate(DonationItem dto, boolean validateNull) {
        List<String> errors = new ArrayList<>();
        validateString(errors, "项目编码", dto.getCode(), null,20, validateNull);
        validateString(errors, "项目名称", dto.getName(), null,60, validateNull);
        validateString(errors, "项目所有者", dto.getOwnerType(), null,60, validateNull);
        return errors;
    }
}
