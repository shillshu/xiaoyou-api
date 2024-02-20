package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.VipKind;
import cn.sibetech.fellow.domain.dto.VipKindQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipKindMapper extends BaseMapper<VipKind> {
    IPage<VipKind> queryPage(Page<VipKind> page, @Param("dto") VipKindQueryDto dto);

    List<VipKind> findList(@Param("dto") VipKind dto);
}
