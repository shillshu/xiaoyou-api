package cn.sibetech.core.mapper;

import cn.sibetech.core.domain.FlowGroup;
import cn.sibetech.core.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liwj
 * @create 2023/9/26
 */
public interface FlowGroupMapper extends BaseMapper<FlowGroup> {
    List<FlowGroup> queryForUser(@Param("userId") String userId);

    List<User> queryUserList(@Param("groups") List<String> groups);
}
