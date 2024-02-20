package cn.sibetech.fellow.domain.validate;

import cn.sibetech.fellow.domain.Donation;
import cn.sibetech.fellow.domain.DonationItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
public class DonationValidate extends BaseValidate{
    public static List<String> validate(Donation dto, boolean validateNull) {
        List<String> errors = new ArrayList<>();
        validateString(errors, "捐赠人", dto.getEntername(), null,20, validateNull);
        validateString(errors, "捐赠时间", dto.getJzsj(), null,20, validateNull);
        validateString(errors, "捐赠方式", dto.getJzfs(), null,60, validateNull);
        validateString(errors, "捐赠类型", dto.getJzlx(), null,20, validateNull);
        validateString(errors, "捐赠实物", dto.getJzsw(), null,20, false);
        validateString(errors, "是否评估", dto.getSfpg(), null,60, false);
        validateString(errors, "用途", dto.getJzyt(), null,20, validateNull);
        validateString(errors, "备注", dto.getBz(), null,200, validateNull);
        return errors;
    }
}
