package cn.sibetech.fellow.api.service;

import cn.sibetech.core.util.DateUtils;
import cn.sibetech.fellow.domain.*;
import cn.sibetech.fellow.service.*;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class ApiAccountServiceImpl implements ApiAccountService {
    @Autowired
    private FellowService fellowService;

    @Autowired
    private FellowAuthInfoService fAuthInfoService;

    @Autowired
    private FellowEducationService fEducationService;

    @Autowired
    private FellowResumeService fResumeService;

    @Autowired
    private FellowBindService bindService;

    /*通过 校友填写的信息 判断是否可以找到唯一*/
    public String findFellow(String xm, String rxnf, String bynf, String csrq, String xh, String xl) {
        List<Fellow> stdList = null;
        /*首先 判断 xm 与 csrq */
        if( StringUtils.isNotEmpty(csrq)) {
            stdList = fellowService.findByXmAndCsrq(xm, csrq);
        }
        if( null != stdList && stdList.size() > 0){
            String fellow_id = stdList.get(0).getId();
            if(stdList.size() == 1 ){
                return fellow_id;
            }else{
                /*判断 入校或毕业年 直接循环 stdList*/
                for(int i = 0; i < stdList.size(); i++){
                    fellow_id = stdList.get(i).getId();
                    if( StringUtils.isNotEmpty(rxnf) || StringUtils.isNotEmpty(bynf) ){
                        List<FellowEducation> eduList = fEducationService.queryByFellowInfo(fellow_id, null, rxnf, bynf);
                        if (null != eduList && eduList.size() == 1 ) {
                            return fellow_id;
                        } else {
                            continue;
                        }
                    }
                }
                return fellow_id;
            }
        }else{/* 根据出生日期 没找到 学生 则根据 入校 或毕业 年份找  如果能唯一确定 则 关联 否则 需要人工审核*/
            if( StringUtils.isNotEmpty(rxnf) || StringUtils.isNotEmpty(bynf) ) {
                List<FellowEducation> eduList = fEducationService.queryByFellowInfo(null, xm, rxnf, bynf);
                if (null != eduList && eduList.size() == 1) {
                    return eduList.get(0).getDataId();
                } else {
                    return null;
                }
            }else{
                return null;
            }
        }
    }

    /*通过 校友填写的信息 判断是否可以找到唯一*/
    @Override
    public String findFellow(String xm, String rxnf, String bynf, String csrq,String xl) {
        List<Fellow> stdList = fellowService.findByXmAndCsrq(xm, null);
        if( null == stdList || stdList.size() == 0 ){
            return null;
        }else if( stdList.size() == 1 ){
            return stdList.get(0).getId();/*根据姓名唯一找到一个 那么就是这个人*/
        }else{/*有多条记录*/
            /* 判断 入校或毕业年份 及学历*/
            int count = 0;
            if( StringUtils.isNotEmpty(rxnf) || StringUtils.isNotEmpty(bynf) ){
                List<Fellow> eduList = fellowService.findByXmAndRxsj(xm, rxnf, bynf);
                if (null != eduList && eduList.size() > 0) {
                    String fellow_id = eduList.get(0).getId();
                    if( eduList.size() == 1 ){
                        count = 1;
                        return fellow_id;
                    }else{
                        for(int i = 0; i < eduList.size(); i++) {
                            Fellow std = eduList.get(i);
                            fellow_id = std.getId();
                            String scsrq = std.getCsrq();
                            if( StringUtils.isNotEmpty(csrq) && StringUtils.isNotEmpty(scsrq) ){
                                try {
                                    if( DateUtils.equalDate(csrq, scsrq) ) {
                                        count = 1;
                                        return fellow_id;
                                    }
                                }catch( Exception e){
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
            if( count == 0){/*没找到学习信息 就看出生年月*/
                if(StringUtils.isNotEmpty(csrq)) {
                    stdList = fellowService.findByXmAndCsrq(xm, csrq);
                    if (null != stdList && stdList.size() > 0) {
                        return stdList.get(0).getId();
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }


    public Map findFellowInfoByFellowId(String fellowId){
        Map resultMap = new HashMap();
        Fellow fellow = fellowService.getById(fellowId);
        if( null != fellow ) {
            resultMap.put("fellow", fellow);
        }

        List<FellowAuthInfo> infoList = fAuthInfoService.findByFellowId(fellowId);

        List<FellowEducation> eduList = fEducationService.queryListByFellowId(fellowId);

        List<FellowResume> resList = fResumeService.queryListByFellowId(fellowId);
        if( null != infoList && infoList.size() > 0 ){
            FellowAuthInfo info =  infoList.get(0);
            resultMap.put("info", info);
        }
        if( null != eduList && eduList.size() > 0){
            resultMap.put("education", eduList.get(0));
        }
        if( null != resList && resList.size() > 0){
            resultMap.put("resume", resList.get(0));
        }
        return resultMap;
    }

    @Override
    public Map findByWxId(String wxId) {
        Map map = new LinkedMap();
        FellowBind bind = bindService.findByOpenId(wxId);
        if (bind != null) {
            FellowAuthInfo authInfo = fAuthInfoService.findByBindId(bind.getId());
            if (null != authInfo) {
                /* 1 需要审核 且 未审核   2 需要审核 且 通过    3 需要审核 且 不通过   4 不需要审核  5 未提交人工审核*/
                map.put("xm", authInfo.getXm());
                map.put("zt", authInfo.getZt());
                map.put("authInfo",authInfo);
            }
            String token = JWTAuthService.issue(bind.getId(), null);
            map.put("token", token);
            map.put("openid", wxId);
        }
        return map;
    }


}
