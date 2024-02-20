package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.FellowBranchGrade;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


public interface FellowBranchGradeService extends IService<FellowBranchGrade> {
    public IPage<FellowBranchGrade> page(FellowBranchGrade dto, int current, int size);
}
