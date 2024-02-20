package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.SmsMsg;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface SmsMsgService extends IService<SmsMsg> {
    public IPage<SmsMsg> page(SmsMsg dto, int current, int size);

    public int getThreadProgress(String id);

    public String add(String content,String typeCode, List<FellowAuthInfo> users);
}
