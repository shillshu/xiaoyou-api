package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.FellowAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface FellowAddressService extends IService<FellowAddress> {
    List<FellowAddress> queryList(FellowAddress address);

    List<FellowAddress> queryListByFellowId(String fellowId);

    Long queryCountFellowId(String fellowId);
}
