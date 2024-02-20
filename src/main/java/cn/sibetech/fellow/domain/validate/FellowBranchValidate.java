package cn.sibetech.fellow.domain.validate;

import cn.sibetech.fellow.domain.BranchSort;
import cn.sibetech.fellow.domain.FellowBranch;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
public class FellowBranchValidate extends BaseValidate{
    public static List<String> validate(FellowBranch dto, boolean validateNull) {
        List<String> errors = new ArrayList<>();
        validateString(errors, "分会名称", dto.getName(), null,60, validateNull);
        return errors;
    }
}
