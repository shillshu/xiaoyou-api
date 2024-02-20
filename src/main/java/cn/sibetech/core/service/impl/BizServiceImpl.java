package cn.sibetech.core.service.impl;

import cn.sibetech.core.domain.Biz;
import cn.sibetech.core.mapper.BizMapper;
import cn.sibetech.core.service.BizService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class BizServiceImpl extends ServiceImpl<BizMapper, Biz> implements BizService {

}
