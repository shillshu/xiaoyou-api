package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.SmsMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


public interface SmsMsgMapper extends BaseMapper<SmsMsg> {
    public IPage<SmsMsg> queryPage(Page<SmsMsg> page, @Param("dto")SmsMsg dto);
}
