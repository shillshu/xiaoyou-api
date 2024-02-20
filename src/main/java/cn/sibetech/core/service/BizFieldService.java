package cn.sibetech.core.service;


import cn.sibetech.core.domain.BizField;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface BizFieldService extends IService<BizField> {
    List<BizField> listTableFields(String bizId);
}
