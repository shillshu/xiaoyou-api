package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.FileDown;
import cn.sibetech.fellow.domain.dto.FileDownQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FileDownMapper extends BaseMapper<FileDown> {
    IPage<FileDown> queryPage(Page<FileDown> page, @Param("dto") FileDownQueryDto dto);

    List<FileDown> queryPage(@Param("dto") FileDownQueryDto dto);
}
