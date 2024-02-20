package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.SmsMsgNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;


public interface SmsMsgNoticeMapper extends BaseMapper<SmsMsgNotice> {
    public IPage<SmsMsgNotice> page(SmsMsgNotice dto, int current, int size);
}
