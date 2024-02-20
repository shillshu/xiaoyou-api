package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.FellowStudent;
import cn.sibetech.fellow.domain.dto.FellowStudentQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FellowStudentMapper extends BaseMapper<FellowStudent> {
    IPage<FellowStudent> queryPage(Page<FellowStudent> page, @Param("fellow") FellowStudentQueryDto dto);

    List<FellowStudent> queryPage(@Param("fellow") FellowStudent dto);
}