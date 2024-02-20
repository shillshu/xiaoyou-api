package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.SmsUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


public interface SmsUserMapper extends BaseMapper<SmsUser> {
    public IPage<FellowAuthInfo> queryUserPage(Page<FellowAuthInfo> page, @Param("dto") FellowAuthInfo dto, @Param("msgId") String msgId);
}
