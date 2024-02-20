package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.SignFellow;
import cn.sibetech.fellow.domain.dto.SignFellowQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface SignFellowService extends IService<SignFellow> {
    public IPage<SignFellow> page(SignFellowQueryDto dto);

    public List<SignFellow> list(SignFellowQueryDto dto);

    public Long countByPc(String pcId);

    public IPage<SignFellow> wqdPage(SignFellowQueryDto dto);

    public List<SignFellow> wqdList(SignFellowQueryDto dto);

    List<SignFellow> findByFellowId(String fellowId);

    SignFellow findByFellowId(String pcId, String fellowId);
}
