package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.Group;
import cn.sibetech.fellow.domain.dto.GroupQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


public interface GroupService extends IService<Group> {
    public IPage<Group> queryPage(GroupQueryDto dto);

    public List<Group> findList(Group dto);

    public List<Map> tree();
}
