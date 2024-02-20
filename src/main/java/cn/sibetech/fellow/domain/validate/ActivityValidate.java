package cn.sibetech.fellow.domain.validate;

import cn.sibetech.fellow.domain.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
public class ActivityValidate extends BaseValidate{
    public static List<String> validate(Activity dto, boolean validateNull) {
        List<String> errors = new ArrayList<>();
        validateString(errors, "活动标题", dto.getName(), null,60, validateNull);
        validateString(errors, "活动类别", dto.getName(), null,60, validateNull);
        validateString(errors, "经费来源", dto.getJfly(), null,60, validateNull);
        validateString(errors, "参与经费金额", dto.getJfje(), null,60, validateNull);
        validateString(errors, "活动联系人", dto.getLxr(), null,60, validateNull);
        validateString(errors, "联系人电话", dto.getLxrdh(), null,60, validateNull);
        validateString(errors, "活动开始时间", dto.getStartTime(), null,20, validateNull);
        validateString(errors, "活动结束时间", dto.getEndTime(), null,20, validateNull);
        validateString(errors, "报名开始时间", dto.getBmStartTime(), null,20, validateNull);
        validateString(errors, "报名结束时间", dto.getBmEndTime(), null,20, validateNull);
        validateString(errors, "是否使用是否需要接站功能", dto.getSfsysfjz(), null,10, validateNull);
        validateString(errors, "活动地址", dto.getPlace(), null,60, validateNull);
        return errors;
    }
}
