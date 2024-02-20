package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.Group;
import cn.sibetech.fellow.domain.dto.GroupQueryDto;
import cn.sibetech.fellow.mapper.GroupMapper;
import cn.sibetech.fellow.service.GroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {
    @Override
    public IPage<Group> queryPage(GroupQueryDto dto) {
        Page<Group> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Group> wrapper = new QueryWrapper<Group>();
        wrapper.orderByAsc("when_created");
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Group> findList(Group dto){
        QueryWrapper<Group> wrapper = new QueryWrapper<Group>();
        wrapper.orderByAsc("when_created");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Map> tree(){
        List<Map> list = new ArrayList<Map>();
        Map gmap = new HashMap();
        gmap.put("id", "group");
        gmap.put("name", "分组管理");
        gmap.put("children", this.findList(new Group()));
        list.add(gmap);
        return list;
    }
}

