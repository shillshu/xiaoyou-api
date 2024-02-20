package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.SignFellow;
import cn.sibetech.fellow.domain.dto.SignFellowQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SignFellowMapper extends BaseMapper<SignFellow> {
    IPage<SignFellow> queryPage(Page<SignFellow> page, @Param("dto") SignFellowQueryDto dto);

    List<SignFellow> queryPage(@Param("dto") SignFellowQueryDto dto);

    IPage<SignFellow> queryWqdPage(Page<SignFellow> page, @Param("dto") SignFellowQueryDto dto);

    List<SignFellow> queryWqdPage(@Param("dto") SignFellowQueryDto dto);

}
