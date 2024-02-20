package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.dto.FellowQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FellowMapper extends BaseMapper<Fellow> {
    IPage<Fellow> queryPage(Page<Fellow> page, @Param("fellow") FellowQueryDto dto);

    List<Fellow> findList(@Param("fellow") Fellow dto);

    List<Fellow> findByXmAndRxsj(@Param("xm") String xm, @Param("rxnf") String rxnf, @Param("bynf") String bynf );
    List<Fellow> queryExportList(@Param("fellow") Fellow dto);
}
