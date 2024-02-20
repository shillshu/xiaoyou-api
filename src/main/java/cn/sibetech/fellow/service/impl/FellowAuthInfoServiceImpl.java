package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.util.DateUtils;
import cn.sibetech.core.util.Identities;
import cn.sibetech.fellow.domain.*;
import cn.sibetech.fellow.domain.dto.FellowAuthInfoQueryDto;
import cn.sibetech.fellow.mapper.FellowAuthInfoMapper;
import cn.sibetech.fellow.service.*;
import cn.sibetech.fellow.util.XyConstant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
public class FellowAuthInfoServiceImpl extends ServiceImpl<FellowAuthInfoMapper, FellowAuthInfo> implements FellowAuthInfoService {
    @Autowired
    private FellowService fellowService;

    @Autowired
    private FellowEducationService fEducationService;

    @Autowired
    private FellowResumeService fResumeService;

    @Autowired
    private FellowAddressService fAddressService;

    @Autowired
    private FellowBranchService fBranchService;

    @Autowired
    private FellowBranchMemberService fbMemberService;

    @Autowired
    private FellowBindService bindService;

    @Autowired
    private FellowAuthjgService jgService;

    @Override
    public IPage<FellowAuthInfo> queryPage(FellowAuthInfoQueryDto dto) {
        Page<FellowAuthInfo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public List<FellowAuthInfo> list(FellowAuthInfoQueryDto dto){
        return baseMapper.queryPage(dto);
    }

    @Override
    public int count(FellowAuthInfo dto){
        return baseMapper.queryCount(dto);
    }

    @Override
    public List<FellowAuthInfo> listDeptFellow(String xyId){
        QueryWrapper<FellowAuthInfo> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(xyId)) {
            wrapper.in("xy_id", xyId.split(","));
            return baseMapper.selectList(wrapper);
        }else{
            return null;
        }
    }

    @Override
    public Long countFellows(List<String> xyIds){
        QueryWrapper<FellowAuthInfo> wrapper = new QueryWrapper<>();
        if(CollectionUtils.isNotEmpty(xyIds)) {
            wrapper.in("xy_id", xyIds);
            wrapper.in("zt", new String[]{"2", "4"});
            return baseMapper.selectCount(wrapper);
        }else{
            return 0l;
        }
    }



    /*得到管理的院系*/
    public String getDeptname(String userId){
        /*String sql = " select d.name from alumni_user_xy_scope s, alumni_department d where s.department_id = d.department_id and s.user_id = '" + userId + "'";
        List<SqlRow> list = ebeanServer.createSqlQuery(sql).findList();
        if( null != list && list.size() > 0 ){
            return list.get(0).getString("name");
        }else {
            return null;
        }*/
        return SecurityContextHolder.getCurrentUser().getDeptId();
    }

    @Override
    public FellowAuthInfo findById(String id) {
        return baseMapper.selectById(id);
    }


    @Override
    public FellowAuthInfo findByBindId(String bindId) {
        QueryWrapper<FellowAuthInfo> wrapper= new QueryWrapper<>();
        wrapper.eq("bind_id",bindId);
        List<FellowAuthInfo> list = baseMapper.selectList(wrapper);
        if( null != list && list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public void add(FellowAuthInfo dto) {
        baseMapper.insert(dto);
    }

    @Override
    public void edit(FellowAuthInfo dto) {
        baseMapper.updateById(dto);
    }

    @Override
    public void editSj(String fellowId, String sj){
        Fellow fellow = new Fellow();
        /*更新校友信息*/
        fellow.setId(fellowId);
        fellow.setYddh2(sj);
        fellowService.updateById(fellow);
    }

    @Override
    public void remove(String[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            List<String> list = Arrays.asList(ids);
            baseMapper.deleteBatchIds(list);
        }
    }

    private Fellow getFellow(FellowAuthInfo authinfo){
        Fellow fellow = new Fellow();
        fellow.setXm(authinfo.getXm());
        fellow.setXb(authinfo.getXb());
        fellow.setCsrq(authinfo.getCsrq());
        fellow.setYddh2(authinfo.getSj());
        fellow.setEmail(authinfo.getEmail());
        fellow.setSfzh(authinfo.getZjhm());
        fellow.setQq(authinfo.getQq());
        fellow.setSzdgb(authinfo.getGj());
        fellow.setSzdsf(authinfo.getSzd());
        fellow.setSzdsx(authinfo.getSzds());
        fellow.setZzmm(authinfo.getZzmm());
        return fellow;
    }

    private FellowEducation getFellowEducation(FellowAuthInfo authinfo){
        FellowEducation edu = new FellowEducation();
        edu.setQssj(authinfo.getRxnf());
        edu.setZzsj(authinfo.getBynf());
        edu.setXl(authinfo.getXl());
        edu.setXh(authinfo.getXh());
        edu.setXy(authinfo.getXy());
        edu.setZy(authinfo.getZy());
        return edu;
    }

    private FellowResume getFellowResume(FellowAuthInfo authinfo){
        FellowResume resume = new FellowResume();
        resume.setHy(authinfo.getHy());
        resume.setGzdw(authinfo.getGzdw());
        return resume;
    }

    private FellowAddress getFellowAddress(FellowAuthInfo authinfo){
        FellowAddress address = new FellowAddress();
        address.setGb(authinfo.getGj());
        address.setSf(authinfo.getSzd());
        address.setSx(authinfo.getSzds());
        address.setIsdefault("是");
        return address;
    }

    /*审批*/
    /*更新bind  更新authinfo  生成authjg 绑定分会 新增校友 及其相关信息*/
    @Override
    public void approveAddFellow(FellowAuthInfo authinfo, String spjg, String spyj){
        String fellowId = Identities.uuid();
        authinfo.setFellowId(fellowId);

        Fellow fellow = this.getFellow(authinfo);
        fellow.setId(fellowId);
        fellow.setMqzt("0");
        fellow.setDataId(fellowId);
        fellowService.save(fellow);

        if( !(StringUtils.isNotEmpty(authinfo.getXylx()) && XyConstant.XYLX_TEACHER.equals(authinfo.getXylx()))) {  /* 教师 不更新 教育经历信息*/
            this.addEducation(authinfo, fellowId);
        }

        if( StringUtils.isNotEmpty(authinfo.getGj()) || StringUtils.isNotEmpty(authinfo.getSzd())){
            /* 维护通讯地址*/
            this.addAddress(authinfo, fellowId);/*只保留一条通讯地址*/
        }
        this.addSpjg(authinfo, spjg, spyj);
        bindService.updateFellow(authinfo.getBindId(), fellowId);
        this.updateFellowInfo(authinfo, spjg);
        this.upFellowResume(authinfo, fellowId);
    }


    /* 更新bind  更新authinfo  生成authjg 绑定分会 更新校友相关信息*/
    @Override
    public void approveEditFellow(FellowAuthInfo authinfo, String ids, String spjg, String spyj){
        authinfo.setFellowId(ids);
        if( !(StringUtils.isNotEmpty(authinfo.getXylx()) && XyConstant.XYLX_TEACHER.equals(authinfo.getXylx()))) {  /* 工作校友 不更新 教育经历信息*/
            /* 查看是否存在教育经历*/
            Long count =  fEducationService.queryCountFellowId(ids);
            if (count == 0) {/* 插入*/
                this.addEducation(authinfo, ids);
            } else {/*更新*/
                /*this.editEducation(authinfo, ids); */ //在审核的时候可以修改教育经历
            }
        }
        if( StringUtils.isNotEmpty(authinfo.getGj()) || StringUtils.isNotEmpty(authinfo.getSzd())){
            /* 维护通讯地址*/
            this.addAddress(authinfo, ids);/*只保留一条通讯地址*/
        }
        this.updateFellow(authinfo);
        this.addSpjg(authinfo, spjg, spyj);
        bindService.updateFellow(authinfo.getBindId(), ids);
        this.updateFellowInfo(authinfo, spjg);
        this.upFellowResume(authinfo, ids);
    }


    /*更新行业信息*/
    private void upFellowResume(FellowAuthInfo authinfo, String fellowId){
        if( StringUtils.isNotEmpty(authinfo.getHy()) || StringUtils.isNotEmpty(authinfo.getGzdw())){
            Long count = fResumeService.queryCountFellowId(fellowId);
            FellowResume resume = this.getFellowResume(authinfo);
            StringBuffer resumeBuffer = new StringBuffer();
            if( count == 0 ){/* 插入*/
                resume.setId(Identities.uuid());
                resume.setDataId(fellowId);
                fResumeService.save(resume);
            }else{/*更新*/
            }
        }
    }


    /*更新校友信息 从临时表 到校友表*/
    public void updateAuthInfoFellow(FellowAuthInfo authInfo){
        if( !(StringUtils.isNotEmpty(authInfo.getXylx()) && XyConstant.XYLX_TEACHER.equals(authInfo.getXylx()))) {  /* 工作校友 不更新 教育经历信息*/
            Long count = fEducationService.queryCountFellowId(authInfo.getFellowId());
            if (count == 0) {/* 插入*/
                this.addEducation(authInfo, authInfo.getFellowId());
            } else {/*更新*/
                /* this.editEducation(authInfo, authInfo.getFellowId());*/ /*教育经历不需要修改*/
            }
        }
        if( StringUtils.isNotEmpty(authInfo.getGj()) || StringUtils.isNotEmpty(authInfo.getSzd())){
            /* 维护通讯地址*/
            this.addAddress(authInfo, authInfo.getFellowId());/*只保留一条通讯地址*/
        }
        this.updateFellow(authInfo);
        this.upFellowResume(authInfo, authInfo.getFellowId());
    }

    /*更新校友信息*/
    private void updateFellow(FellowAuthInfo authinfo){
        Fellow fellow = this.getFellow(authinfo);
        fellow.setId(authinfo.getFellowId());
        fellowService.updateById(fellow);
    }

    private void addAddress(FellowAuthInfo authinfo, String fellowId){
        FellowAddress address = this.getFellowAddress(authinfo);
        address.setDataId(fellowId);
        address.setId(Identities.uuid());
        fAddressService.save(address);
    }


    /*审核不通过*/
    @Override
    public void noPassFellow(FellowAuthInfo authinfo, String spjg, String spyj){
        this.updateFellowInfo(authinfo, spjg);
        this.addSpjg(authinfo, spjg, spyj);
    }

    private void addEducation(FellowAuthInfo authinfo, String fellowId){
        StringBuffer eduBuffer = new StringBuffer();
        FellowEducation education = this.getFellowEducation(authinfo);
        education.setDataId(fellowId);
        education.setId(Identities.uuid());
        fEducationService.save(education);
    }


    private void addSpjg(FellowAuthInfo authinfo, String spjg, String spyj){
        FellowAuthjg authjg = new FellowAuthjg();
        authjg.setId(Identities.uuid());
        authjg.setInfoId(authinfo.getId());
        authjg.setSpzt("1");
        authjg.setSpjg(spjg);
        authjg.setSpyj(spyj);
        authjg.setSpsj(DateUtils.getCurrentDate());
        jgService.save(authjg);
    }

    private void updateFellowInfo(FellowAuthInfo authInfo, String spjg){
        if("1".equals(spjg)){
            authInfo.setZt("2");
            /*加入对应的校友分会*/
            this.updateBranch(authInfo);
        }else{
            authInfo.setZt("3");
        }
        this.updateById(authInfo);
    }


    private void updateBranch(FellowAuthInfo authInfo){
        if( StringUtils.isNotEmpty(authInfo.getXyfh())) {
            this.addFellowBranch(authInfo.getFellowId(), authInfo.getXyfh());
        }
        if( StringUtils.isNotEmpty(authInfo.getXyfh1())) {
            this.addFellowBranch(authInfo.getFellowId(), authInfo.getXyfh1());
        }
        if( StringUtils.isNotEmpty(authInfo.getDffh())) {
            this.addFellowBranch(authInfo.getFellowId(), authInfo.getDffh());
        }
        if( StringUtils.isNotEmpty(authInfo.getDffh1())) {
            this.addFellowBranch(authInfo.getFellowId(), authInfo.getDffh1());
        }
        if( StringUtils.isNotEmpty(authInfo.getHyfh())) {
            this.addFellowBranch(authInfo.getFellowId(), authInfo.getHyfh());
        }
        if( StringUtils.isNotEmpty(authInfo.getHyfh1())) {
            this.addFellowBranch(authInfo.getFellowId(), authInfo.getHyfh1());
        }
    }

    /*加入校友分会*/
    @Override
    public void addFellowBranch(String fellowId, String szd, String szds, String hy, String xy){
        List<FellowBranch> list = fBranchService.getBranchList(szd, szds,  hy, xy);
        for(int i = 0; i < list.size(); i++){
            FellowBranch branch = list.get(i);
            String branchId = branch.getId();
            /*直接加入校友分会*/
            try {
                this.addFellowBranch(fellowId, branchId);
            }catch(Exception e){
                continue;
            }
        }
    }

    /*直接加入校友分会*/
    @Override
    public void addFellowBranch(String fellowId, String branchId){
        Long count = fbMemberService.queryCountFellowId(fellowId, branchId);
        if( count == 0 ) {
            fbMemberService.addBranchMember(fellowId, branchId);
        }
    }


    /*查看校友被绑定信息*/
    @Override
    public List<FellowAuthInfo> findByFellowId(String fellowId){
        QueryWrapper<FellowAuthInfo> wrapper= new QueryWrapper<>();
        wrapper.eq("fellow_id",fellowId);
        return baseMapper.selectList(wrapper);
    }


    /*查看校友是否被绑定*/
    public boolean isBindFellowId(String fellowId){
        List<FellowAuthInfo> dataList = this.findByFellowId(fellowId);
        if( null == dataList || dataList.size() == 0 ){
            return false;
        }else{
            return true;
        }
    }

    /*解绑 根据fellowId*/
    public void unBind(String fellowId){
        String updateRzSql = "update xy_fellow_authinfo set shbz = '被管理员解绑' , zt = '1' , fellow_id = '' where fellow_id = '" + fellowId + "' ";
        String updateBindSwl = " update xy_fellow_bind set fellow_id = '' where fellow_id = '" + fellowId + "' ";
        baseMapper.updateData(updateRzSql);
        baseMapper.updateData(updateBindSwl);
    }

    @Override
    public List<Map>  queryData(String sql){
        return baseMapper.queryData(sql);
    }

    /*人工审核时  查找同名的 校友*/
    @Override
    public List<Map> getFellowList(String xm, String xy, String rxnf, String bynf){
        String selectSql = " SELECT f.id, f.xm, f.xb, f.csrq , e.qssj, e.zzsj, e.xy, e.zy, e.xh, e.xl, CASE WHEN a.id IS not NULL THEN '1' ELSE '0' END AS zczt \n" +
                "  FROM xy_fellow f left join xy_fellow_education e \n" +
                "  on f.id = e.data_id LEFT JOIN xy_fellow_authinfo a ON f.id = a.fellow_id   where f.xm = '" + xm + "'";
    /*if(StringUtils.isNotEmpty( xy )){
        selectSql += " and e.xy = '" + xy + "'";
    }
    if(StringUtils.isNotEmpty( rxnf )){
        selectSql += " and substr(qssj, 1, 4) = '" + rxnf + "'";
    }
    if(StringUtils.isNotEmpty( bynf )){
        selectSql += " and substr(zzsj, 1, 4) = '" + bynf + "'";
    }*/
        List<Map> stdList = baseMapper.queryData(selectSql);
        return stdList;
    }
}

