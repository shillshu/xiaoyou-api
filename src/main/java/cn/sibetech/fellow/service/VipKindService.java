package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.VipKind;
import cn.sibetech.fellow.domain.dto.VipKindQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


public interface VipKindService extends IService<VipKind> {
    public IPage<VipKind> queryPage(VipKindQueryDto dto);

    public List<VipKind> findList(VipKind dto);

    public List<VipKind> findList(String lx);

    public List<Map> tree();
}
