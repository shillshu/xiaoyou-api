package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.domain.Role;
import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.util.RoleUtil;
import cn.sibetech.fellow.domain.VipKind;
import cn.sibetech.fellow.domain.dto.VipKindQueryDto;
import cn.sibetech.fellow.mapper.VipKindMapper;
import cn.sibetech.fellow.service.VipKindService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class VipKindServiceImpl extends ServiceImpl<VipKindMapper, VipKind> implements VipKindService {
    @Override
    public IPage<VipKind> queryPage(VipKindQueryDto dto) {
        Page<VipKind> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public List<VipKind> findList(VipKind dto){
        return baseMapper.findList(dto);
    }

    @Override
    public List<VipKind> findList(String lx){
        VipKind dto = new VipKind();
        Role role = SecurityContextHolder.getCurrentUser().getCurrentRole();
        if(!RoleUtil.isSysAdmin(role)){
            String xyId = SecurityContextHolder.getCurrentUser().getDeptId();
            dto.setOwner(xyId);
        }
        dto.setOwnerType(lx);
        return baseMapper.findList(dto);
    }

    @Override
    public List<Map> tree(){
        List<Map> list = new ArrayList<Map>();
        Map gmap = new HashMap();
        gmap.put("id", "global");
        gmap.put("mc", "校级");
        gmap.put("children", this.findList("global"));
        list.add(gmap);
        Map dMap = new HashMap();
        dMap.put("id", "dept");
        dMap.put("mc", "学院");
        dMap.put("children", this.findList("dept"));
        list.add(dMap);
        Map bmap = new HashMap();
        bmap.put("id", "branch");
        bmap.put("mc", "分会");
        bmap.put("children", this.findList("branch"));
        list.add(bmap);
        return list;
    }
}

