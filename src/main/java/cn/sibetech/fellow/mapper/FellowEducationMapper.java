package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.FellowEducation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FellowEducationMapper extends BaseMapper<FellowEducation> {
    List<FellowEducation> queryList(@Param("edu") FellowEducation dto);

    List<FellowEducation> queryByDataId(@Param("dataId") String dataId);

    List<FellowEducation> queryByFellowInfo(@Param("fellowId")String fellowId, @Param("xm")String xm, @Param("xh")String xh, @Param("rxnf")String rxnf, @Param("bynf")String bynf);
}
