package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.FellowBranchMember;
import cn.sibetech.fellow.domain.dto.FellowQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface FellowBranchMemberService extends IService<FellowBranchMember> {
    public IPage<Fellow> page(FellowQueryDto dto, String branchId);

    void remove(String fellowId, String kindId);

    boolean isFellowInBranch(String branchId, String fellowId);

    List<FellowBranchMember> queryListByFellowId(String fellowId, String branchId);

    Long queryCountFellowId(String fellowId, String branchId);

    void addBranchMember(String fellowId, String branchId);

    int addBatch(List<String> fellowIds, String branchId);
}
