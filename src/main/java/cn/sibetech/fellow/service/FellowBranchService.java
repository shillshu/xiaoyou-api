package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.FellowBranch;
import cn.sibetech.fellow.domain.dto.FellowBranchQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


public interface FellowBranchService extends IService<FellowBranch> {
    public IPage<FellowBranch> queryPage(FellowBranchQueryDto dto);

    public List<FellowBranch> getBranchList(String type, String deptId);

    List<FellowBranch> getFellowBranchList(String fellowId);

    List<FellowBranch> getBranchList(String szds, String szd, String xy, String hy);

    List<FellowBranch> getAllBranchList(String lb);

    List<Map<String, Object>> tree(String type);
}
