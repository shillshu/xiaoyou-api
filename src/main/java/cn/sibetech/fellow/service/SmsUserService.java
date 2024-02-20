package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.SmsUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


public interface SmsUserService extends IService<SmsUser> {

    public IPage<FellowAuthInfo> page(FellowAuthInfo dto, String msgId, int current, int size);
}
