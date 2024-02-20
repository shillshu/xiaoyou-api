package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.FellowResume;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FellowResumeMapper extends BaseMapper<FellowResume> {
    List<FellowResume> queryList(@Param("resume") FellowResume dto);

    List<FellowResume> queryByDataId(@Param("dataId") String dataId);

}
