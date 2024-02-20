package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.util.Identities;
import cn.sibetech.fellow.domain.FellowBind;
import cn.sibetech.fellow.mapper.FellowBindMapper;
import cn.sibetech.fellow.service.FellowBindService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FellowBindServiceImpl extends ServiceImpl<FellowBindMapper, FellowBind> implements FellowBindService {
    @Override
    public FellowBind findByBindId(String bindId){
        QueryWrapper<FellowBind> wrapper= new QueryWrapper<>();
        wrapper.eq("open_id",bindId);
        List<FellowBind> list = baseMapper.selectList(wrapper);
        if( null == list  || list.size() == 0 ){
            return null;
        }else{
            return list.get(0);
        }
    }
    /*给绑定信息 维护校友ID*/
    @Override
    public void updateFellow(String id, String fellowId){
        FellowBind bind = baseMapper.selectById(id);
        if( null != bind ){
            bind.setFellowId(fellowId);
            baseMapper.updateById(bind);
        }
    }
    @Override
    public FellowBind findByOpenId(String openId){
        QueryWrapper<FellowBind> wrapper= new QueryWrapper<>();
        wrapper.eq("open_id",openId);
        List<FellowBind> list = baseMapper.selectList(wrapper);
        if( null == list  || list.size() == 0 ){
            return null;
        }else{
            return list.get(0);
        }
    }

    /*绑定信息*/
    @Override
    public String bind(String bindId, String fellowId){
        FellowBind bind = new FellowBind();
        bind.setId(Identities.uuid());
        bind.setOpenId(bindId);
        bind.setFellowId(fellowId);
        baseMapper.insert(bind);
        return bind.getId();
    }

    /*绑定信息*/
    @Override
    public void unBind(String id){
    }
}

