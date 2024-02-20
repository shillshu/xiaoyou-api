package cn.sibetech.core.service.impl;

import cn.sibetech.core.domain.BizField;
import cn.sibetech.core.mapper.BizFieldMapper;
import cn.sibetech.core.service.BizFieldService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BizFieldServiceImpl extends ServiceImpl<BizFieldMapper, BizField> implements BizFieldService {
    public List<BizField> listTableFields(String bizId) {
        BizField field = new BizField();
        field.setBizId(bizId);
        List<BizField> list = (baseMapper).queryList(field);
        return list;
    }
}
