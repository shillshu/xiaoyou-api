package cn.sibetech.core.mapper;

import cn.sibetech.core.domain.BizField;
import cn.sibetech.core.domain.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BizFieldMapper extends BaseMapper<BizField> {
    List<BizField> queryList(@Param("field") BizField dto);
}
