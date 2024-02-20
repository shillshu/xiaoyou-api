package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.BranchSort;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


public interface BranchSortService extends IService<BranchSort> {
    public List<Map<String, Object>> children(String fatherId);

    List<Map<String, Object>> tree(String type);
}
