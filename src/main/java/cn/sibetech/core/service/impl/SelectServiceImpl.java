package cn.sibetech.core.service.impl;

import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.mapper.SelectMapper;
import cn.sibetech.core.service.SelectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class SelectServiceImpl implements SelectService {
    @Resource
    private SelectMapper baseMapper;
    @Override
    public List<SelectModel> selectDeptList(String fatherId){
        return baseMapper.selectDeptList(fatherId);
    }

    @Override
    public  List<SelectModel> selectXueyuanList(){
        return baseMapper.selectXueyuanList();
    }

    @Override
    public  List<SelectModel> selectMajorList(){
        return baseMapper.selectMajorList(null);
    }

    @Override
    public  List<SelectModel> selectMajorList(String xyId){
        return baseMapper.selectMajorList(xyId);
    }

    @Override
    public  List<SelectModel> selectClassList(){
        return baseMapper.selectClassList(null, null, null);
    }

    @Override
    public  List<SelectModel> selectClassList(String xyId, String zyId, String nj){
        return baseMapper.selectClassList(xyId, zyId, nj);
    }

    @Override
    public  List<SelectModel> selectNjList(){
        List<SelectModel> njlist = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR) + 1;
        for (int i = 0; i < 10; i++) {
            SelectModel model = new SelectModel();
            model.setLabel(String.valueOf(currentYear - i));
            model.setValue(model.getLabel());
            njlist.add(model);
        }
        return njlist;
    }

    @Override
    public  List<SelectModel> selectCodeItemList(String codeKindId){
        return baseMapper.selectCodeItemList(codeKindId);
    }


}
