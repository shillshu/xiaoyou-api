package cn.sibetech.core.mapper;

import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.domain.User;
import cn.sibetech.core.domain.dto.UserQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    User selectByUsername(@Param("username") String username);

    List<SelectModel> selectDeptList(@Param("fatherId") String fatherId);

    List<String> queryDeptUser(@Param("roleId")String roleId,@Param("deptId") String deptId);

    List<String> queryRoleUser(@Param("roleId")String roleId);
    IPage<User> queryPage(Page<User> page,@Param("dto") UserQueryDto dto);

    List<User> queryByIds(@Param("userIds") List<String> userIds);
}
