package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.FellowAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FellowAddressMapper extends BaseMapper<FellowAddress> {
    List<FellowAddress> queryList(@Param("address") FellowAddress dto);
}
