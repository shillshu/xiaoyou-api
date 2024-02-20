package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.Sign;
import cn.sibetech.fellow.domain.dto.SignQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


public interface SignService extends IService<Sign> {

    public IPage<Sign> queryPage(SignQueryDto dto);
    public void editStatus(String ids, String status);
}
