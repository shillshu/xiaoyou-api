package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.SmsUser;
import cn.sibetech.fellow.mapper.SmsUserMapper;
import cn.sibetech.fellow.service.SmsUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class SmsUserServiceImpl extends ServiceImpl<SmsUserMapper, SmsUser> implements SmsUserService {

    @Override
    public IPage<FellowAuthInfo> page(FellowAuthInfo dto, String msgId, int current, int size){
        Page<FellowAuthInfo> page = new Page<FellowAuthInfo>(current, size);
        return baseMapper.queryUserPage(page, dto, msgId);
    }

}

