package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.SmsMsgNotice;
import cn.sibetech.fellow.mapper.SmsMsgNoticeMapper;
import cn.sibetech.fellow.service.SmsMsgNoticeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class SmsMsgNoticeServiceImpl extends ServiceImpl<SmsMsgNoticeMapper, SmsMsgNotice> implements SmsMsgNoticeService {

    @Override
    public IPage<SmsMsgNotice> page(SmsMsgNotice dto, int current, int size) {
        return baseMapper.page(dto, current, size);
    }


}

