package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.util.Identities;
import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.FellowEducation;
import cn.sibetech.fellow.domain.FellowStudent;
import cn.sibetech.fellow.domain.dto.FellowStudentQueryDto;
import cn.sibetech.fellow.mapper.FellowStudentMapper;
import cn.sibetech.fellow.service.FellowEducationService;
import cn.sibetech.fellow.service.FellowService;
import cn.sibetech.fellow.service.FellowStudentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FellowStudentServiceImpl extends ServiceImpl<FellowStudentMapper, FellowStudent> implements FellowStudentService {
    @Autowired
    private FellowService fellowService;

    @Autowired
    private FellowEducationService fEducationService;

    @Override
    public IPage<FellowStudent> queryPage(FellowStudentQueryDto dto) {
        Page<FellowStudent> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public List<FellowStudent> findList(FellowStudent dto){
        return baseMapper.queryPage(dto);
    }

    @Override
    public int syncAllStudent(FellowStudent dto){
        List<FellowStudent> studentList = this.findList(dto);
        int count = 0;
        if( null != studentList && studentList.size() > 0 ) {
            for( FellowStudent student : studentList ){
                if(syncOneStudent(student)){
                    count++;
                }
            }
        }
        return count;
    }

    /* 同步一位同学到校友信息中*/
    /* 以 姓名+学号 或者是证件号码为唯一判断*/
    @Override
    public boolean syncOneStudent(String id){
        FellowStudent student = baseMapper.selectById(id);
        return syncOneStudent(student);
    }

    /* 同步一位同学到校友信息中*/
    /* 以 姓名+学号 或者是证件号码为唯一判断*/
    public boolean syncOneStudent(FellowStudent student){
        if( null != student ){
            /*判断校友是否存在*/
            String edutionFellowId = fEducationService.findByXmAndXh(student.getXm(), student.getXh());
            if(StringUtils.isNotEmpty(edutionFellowId)){
                return true;
            }else{
                String zjhmFellowId = "";
                if( StringUtils.isNotEmpty(student.getZjhm())){
                    zjhmFellowId = fellowService.findByZjhm(student.getZjhm()); /* 存在证件号码 不存在教育经历*/
                }
                if( StringUtils.isNotEmpty(zjhmFellowId)){
                    /* 证件号码存在 则查学号 和fellowId 因为姓名有可能改过 */
                    String zjhmEducationFellowId = fEducationService.findByFellowIdAndXh(zjhmFellowId, student.getXh());
                    if( StringUtils.isEmpty(zjhmEducationFellowId)){
                        addEducation(student, zjhmFellowId);/* 存在证件号码 不存在教育经历 直接新增教育经历即可*/
                        return true;
                    }else{
                        /* 否则说明 改了姓名*/
                        Fellow xmFellow = new Fellow();
                        xmFellow.setId(zjhmFellowId);
                        xmFellow.setXm(student.getXm());
                        fellowService.updateById(xmFellow);
                        return true;
                    }
                }else{
                    /* 如果不存在 新增校友信息 与校友教育信息*/
                    Fellow fellow = this.getFellow(student);
                    if( StringUtils.isNotEmpty(student.getZjhm())){
                        fellow.setId(student.getZjhm());
                    }else{
                        fellow.setId(student.getXh());
                    }
                    fellow.setMqzt("0");
                    fellow.setDataId(fellow.getId());
                    fellowService.save(fellow);
                    addEducation(student, fellow.getId());
                    return true;
                }
            }
        }else{
            return false;
        }
    }

    private Fellow getFellow(FellowStudent student){
        Fellow fellow = new Fellow();
        fellow.setXm(student.getXm());
        fellow.setXb(student.getXb());
        fellow.setCsrq(student.getCsrq());
        fellow.setYddh2(student.getLxdh());
        fellow.setEmail(student.getEmail());
        fellow.setSfzh(student.getZjhm());
        fellow.setQq(student.getQqh());
        fellow.setSzdsf(student.getSzd());
        fellow.setZzmm(student.getZzmm());
        return fellow;
    }

    private FellowEducation getFellowEducation(FellowStudent student){
        FellowEducation edu = new FellowEducation();
        edu.setQssj(student.getRxsj());
        edu.setZzsj(student.getLxsj());
        edu.setXl(student.getPycc());
        edu.setXh(student.getXh());
        edu.setXy(student.getXy());
        edu.setZy(student.getZy());
        return edu;
    }

    private void addEducation(FellowStudent student, String fellowId){
        FellowEducation education = this.getFellowEducation(student);
        education.setDataId(fellowId);
        education.setId(Identities.uuid());
        fEducationService.save(education);
    }


}

