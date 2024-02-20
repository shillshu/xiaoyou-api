package cn.sibetech.core.service;

import cn.sibetech.core.domain.SelectModel;

import java.util.List;

public interface SelectService {
    List<SelectModel> selectDeptList(String fatherId);

    List<SelectModel> selectXueyuanList();

    List<SelectModel> selectMajorList();

    List<SelectModel> selectMajorList(String xyId);

    List<SelectModel> selectClassList();

    List<SelectModel> selectClassList(String xyId, String zyId, String nj);

    List<SelectModel> selectNjList();

    List<SelectModel> selectCodeItemList(String codeKindId);


}
