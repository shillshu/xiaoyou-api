package cn.sibetech.core.mapper;

import cn.sibetech.core.domain.DeptClass;
import cn.sibetech.core.domain.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserRoleMapper extends BaseMapper<UserRole> {
    List<UserRole> queryUserRole(@Param("userId")String userId, @Param("roleId") String roleId);

    List<DeptClass> queryClass(@Param("scope")String scope);

    List<DeptClass> queryUserClass(@Param("userId")String userId, @Param("type")String type);
}
