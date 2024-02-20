package cn.sibetech.core.mapper;

import cn.sibetech.core.domain.SelectModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SelectMapper extends BaseMapper<SelectModel> {
    List<SelectModel> selectDeptList(@Param("fatherId") String fatherId);

    List<SelectModel> selectXueyuanList();

    List<SelectModel> selectMajorList(@Param("xyId") String xyId);

    List<SelectModel> selectClassList(@Param("xyId") String xyId, @Param("zyId") String zyId, @Param("nj") String nj);


    List<SelectModel> selectCodeItemList(@Param("codeKindId") String codeKindId);
}
