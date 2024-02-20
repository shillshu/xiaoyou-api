package cn.sibetech.core.service;

import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.domain.ShiroUser;
import cn.sibetech.core.domain.User;
import cn.sibetech.core.domain.dto.UserQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {
    IPage<User> queryPage(UserQueryDto dto);

    List<SelectModel> selectDeptList(String fatherId);

    void add(User dto);

    void edit(User dto);

    User findById(String id);

    List<User> queryByIds(List<String> ids);

    List<User> queryFlowGroupUser(List<String> groups);

    ShiroUser queryUserInfo(String username);
}
