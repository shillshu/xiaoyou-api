package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.FellowVipMember;
import cn.sibetech.fellow.domain.dto.FellowQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface FellowVipMemberService extends IService<FellowVipMember> {
    public IPage<Fellow> page(FellowQueryDto dto, String kindId);

    Long countByFellowId(String kindId, String fellowId);

    boolean isFellowInKind(String kindId, String fellowId);

    void remove(String fellowId, String kindId);

    public int addBatch(List<String> fellowIds, String kindId);
}
