package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.domain.Role;
import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.util.RoleUtil;
import cn.sibetech.fellow.domain.BranchSort;
import cn.sibetech.fellow.domain.FellowBranch;
import cn.sibetech.fellow.mapper.BranchSortMapper;
import cn.sibetech.fellow.service.BranchSortService;
import cn.sibetech.fellow.service.FellowBranchService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BranchSortServiceImpl extends ServiceImpl<BranchSortMapper, BranchSort> implements BranchSortService {
    @Autowired
    private FellowBranchService branchService;


    public List<BranchSort> getAllSortList(){
        QueryWrapper<BranchSort> wrapper = new QueryWrapper<BranchSort>();
        wrapper.orderByAsc("order_number");
        return baseMapper.selectList(wrapper);
    }

    /*第一代 子节点以type 作为fatherId*/
    @Override
    public List<Map<String, Object>> children(String fatherId) {
        List<BranchSort> list = this.getAllSortList();
        List<FellowBranch> branchList = branchService.getAllBranchList(null);
        return this.getChildren(list, branchList, fatherId);
    }

    /*第一代 子节点以type 作为fatherId*/
    @Override
    public List<Map<String, Object>> tree(String type) {
        List<BranchSort> list = this.getAllSortList();
        String deptId = "";
        Role role = SecurityContextHolder.getCurrentUser().getCurrentRole();
        if(!RoleUtil.isSysAdmin(role)){
            deptId = SecurityContextHolder.getCurrentUser().getDeptId();
        }
        List<FellowBranch> branchList = branchService.getBranchList(type, deptId);
        return this.getChildren(list, branchList, type);
    }

    private List<Map<String, Object>> getChildren(List<BranchSort> sorts,List<FellowBranch> branchList, String fid) {
        List<Map<String, Object>> resultList = new ArrayList();
        Iterator var4 = sorts.iterator();
        while(var4.hasNext()) {
            BranchSort sort = (BranchSort)var4.next();
            if (StringUtils.isNotEmpty(sort.getFatherId()) && sort.getFatherId().equals(fid)) {
                Map<String, Object> map = new HashMap();
                map.put("id", sort.getId());
                map.put("fId", fid);
                map.put("label", sort.getName());
                map.put("lb", "sort");
                List<Map<String, Object>> children = this.getChildren(sorts, branchList, sort.getId());
                if (children != null && children.size() > 0) {
                    map.put("children", children);
                }
                resultList.add(map);
            }
        }
        Iterator var = branchList.iterator();
        while(var.hasNext()) {
            FellowBranch branch = (FellowBranch)var.next();
            if (StringUtils.isNotEmpty(branch.getFatherId()) && branch.getFatherId().equals(fid)) {
                Map<String, Object> map = new HashMap();
                map.put("id", branch.getId());
                map.put("label", branch.getName());
                map.put("lb", "branch");
                map.put("fId", fid);
                resultList.add(map);
            }
        }
        return resultList;
    }
}

