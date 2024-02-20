package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.FellowBind;
import com.baomidou.mybatisplus.extension.service.IService;


public interface FellowBindService extends IService<FellowBind> {
     FellowBind findByBindId(String bindId);

     FellowBind findByOpenId(String openId);

     void updateFellow(String bindId, String fellowId);

     String bind(String bindId, String fellowId);

     void unBind(String id);
}
