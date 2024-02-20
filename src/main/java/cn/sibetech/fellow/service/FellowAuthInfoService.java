package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.dto.FellowAuthInfoQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


public interface FellowAuthInfoService extends IService<FellowAuthInfo> {
    public IPage<FellowAuthInfo> queryPage(FellowAuthInfoQueryDto dto);

    public int count(FellowAuthInfo dto);

    public List<FellowAuthInfo> list(FellowAuthInfoQueryDto dto);

    public Long countFellows(List<String> xyIds);

    public List<FellowAuthInfo> listDeptFellow(String xy);

    FellowAuthInfo findById(String id);

    void add(FellowAuthInfo dto);

    void edit(FellowAuthInfo dto);

    void editSj(String fellowId, String sj);

    void remove(String[] ids);

    public FellowAuthInfo findByBindId(String bindId);

    /*审批*/
    void approveAddFellow(FellowAuthInfo authinfo, String spjg, String spyj);

    void approveEditFellow(FellowAuthInfo authinfo, String ids, String spjg, String spyj);


    /*审核不通过*/
    void noPassFellow(FellowAuthInfo authinfo, String spjg, String spyj);

    /*加入校友分会*/
    public void addFellowBranch(String fellowId, String szd, String szds, String hy, String xy);

    /*加入校友分会*/
    public void addFellowBranch(String fellowId, String branchId);

    /*更新校友信息 从临时表 到校友表*/
    public void updateAuthInfoFellow(FellowAuthInfo authInfo);

    /*查看校友被绑定信息*/
    public List<FellowAuthInfo> findByFellowId(String fellowId);

    /*查看校友是否被绑定*/
    public boolean isBindFellowId(String fellowId);

    /*解绑 根据fellowId*/
    public void unBind(String fellowId);

    List<Map>  queryData(String sql);

    /*人工审核时  查找同名的 校友*/
    public List<Map> getFellowList(String xm, String xy, String rxnf, String bynf);
}
