package cn.sibetech.core.mapper;

import cn.sibetech.core.domain.Biz;
import cn.sibetech.core.domain.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BizMapper extends BaseMapper<Biz> {
    List<Biz> queryList(@Param("biz") Biz dto);
}
