package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.dto.FellowAuthInfoQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FellowAuthInfoMapper extends BaseMapper<FellowAuthInfo> {

    IPage<FellowAuthInfo> queryPage(Page<FellowAuthInfo> page, @Param("info") FellowAuthInfoQueryDto dto);

    List<FellowAuthInfo> queryPage(@Param("info") FellowAuthInfoQueryDto dto);

    int  queryCount(@Param("info") FellowAuthInfo dto);

    /*根据sql 查询数据表的数据*/
    List<Map> queryData(@Param("sqlStr") String sql);

    /*根据sql 更新数据表的数据*/
    void updateData(@Param("sqlStr") String sql);

}
