package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.domain.Role;
import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.util.RoleUtil;
import cn.sibetech.fellow.domain.FellowBranch;
import cn.sibetech.fellow.domain.dto.FellowBranchQueryDto;
import cn.sibetech.fellow.mapper.FellowBranchMapper;
import cn.sibetech.fellow.service.FellowBranchService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class FellowBranchServiceImpl extends ServiceImpl<FellowBranchMapper, FellowBranch> implements FellowBranchService {
    private static final String root_father_id = "0";

    @Override
    public IPage<FellowBranch> queryPage(FellowBranchQueryDto dto) {
        Page<FellowBranch> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public List<FellowBranch> getFellowBranchList(String fellowId){
        return baseMapper.queryFellowBranchList(fellowId);
    }

    @Override
    public List<FellowBranch> getBranchList(String szds, String szd, String xy, String hy){
        List<String> nameList = new ArrayList<>();
        nameList.add(szds);
        nameList.add(szd);
        nameList.add(xy);
        nameList.add(hy);
        QueryWrapper<FellowBranch> wrapper= new QueryWrapper<>();
        wrapper.in("name", nameList);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<FellowBranch> getAllBranchList(String type){
        FellowBranch branch = new FellowBranch();
        if(StringUtils.isNotEmpty(type)){
            branch.setType(type);
        }
        return baseMapper.queryList(branch);
    }

    @Override
    public List<FellowBranch> getBranchList(String type, String deptId){
        FellowBranch branch = new FellowBranch();
        if(StringUtils.isNotEmpty(type)){
            branch.setType(type);
        }
        if(StringUtils.isNotEmpty(deptId)){
            branch.setOwner(deptId);
        }
        return baseMapper.queryList(branch);
    }

    public List<Map<String, Object>> tree(String type) {
        String deptId = "";
        Role role = SecurityContextHolder.getCurrentUser().getCurrentRole();
        if(!RoleUtil.isSysAdmin(role)){
            deptId = SecurityContextHolder.getCurrentUser().getDeptId();
        }
        List<FellowBranch> list = this.getBranchList(type, deptId);
        return this.getChildren(list, root_father_id, type);
    }

    private List<Map<String, Object>> getChildren(List<FellowBranch> branches, String fid, String type) {
        List<Map<String, Object>> resultList = new ArrayList();
        Iterator var4 = branches.iterator();

        while(var4.hasNext()) {
            FellowBranch branch = (FellowBranch)var4.next();
            if (StringUtils.isNotEmpty(branch.getFatherId()) && branch.getFatherId().equals(fid) && StringUtils.isNotEmpty(type) && type.equals(branch.getType())) {
                Map<String, Object> map = new HashMap();
                map.put("id", branch.getId());
                map.put("label", branch.getName());
                List<Map<String, Object>> children = this.getChildren(branches, branch.getId(), type);
                if (children != null && children.size() > 0) {
                    map.put("children", children);
                }

                resultList.add(map);
            }
        }
        return resultList;
    }
}

