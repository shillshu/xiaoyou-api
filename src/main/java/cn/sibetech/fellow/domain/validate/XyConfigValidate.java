package cn.sibetech.fellow.domain.validate;

import cn.sibetech.fellow.domain.Group;
import cn.sibetech.fellow.domain.XyConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
public class XyConfigValidate extends BaseValidate{
    public static List<String> validate(XyConfig dto, boolean validateNull) {
        List<String> errors = new ArrayList<>();
        validateString(errors, "KEY", dto.getKey(), null,16, validateNull);
        validateString(errors, "值", dto.getValue(), null,100, validateNull);
        validateString(errors, "类型", dto.getType(), null,100, validateNull);
        return errors;
    }
}
