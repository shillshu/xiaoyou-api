package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.SmsMsgNotice;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


public interface SmsMsgNoticeService extends IService<SmsMsgNotice> {
    public IPage<SmsMsgNotice> page(SmsMsgNotice dto, int current, int size);
}
