package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.util.Identities;
import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.FellowEducation;
import cn.sibetech.fellow.domain.FellowResume;
import cn.sibetech.fellow.domain.dto.FellowQueryDto;
import cn.sibetech.fellow.mapper.FellowEducationMapper;
import cn.sibetech.fellow.mapper.FellowMapper;
import cn.sibetech.fellow.mapper.FellowResumeMapper;
import cn.sibetech.fellow.service.FellowService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class FellowServiceImpl extends ServiceImpl<FellowMapper, Fellow> implements FellowService {

    @Resource
    private FellowEducationMapper educationMapper;

    @Resource
    private FellowResumeMapper resumeMapper;

    @Override
    public IPage<Fellow> queryPage(FellowQueryDto dto) {
        Page<Fellow> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public List<Fellow> findList(Fellow dto){
        return baseMapper.findList(dto);
    }

    @Override
    public List<Fellow> findExportList(Fellow dto){
        return baseMapper.queryExportList(dto);
    }
    @Override
    public List<Fellow> findByXmAndCsrq(String xm, String csrq){
        Fellow fellow = new Fellow();
        fellow.setXm(xm);
        fellow.setCsrq(csrq);
        return this.findList(fellow);
    }

    @Override
    public List<Fellow> findByXmAndRxsj(String xm, String rxnf, String bynf){
        return baseMapper.findByXmAndRxsj(xm, rxnf, bynf);
    }


    @Override
    public void addFellow(Fellow dto){
        String fellowId = Identities.uuid();
        dto.setId(fellowId);
        if(CollectionUtils.isNotEmpty(dto.getResumeList())){
            dto.getResumeList().stream().forEach( resume -> {
                resume.setId(Identities.uuid());
                resume.setDataId(fellowId);
                resumeMapper.insert(resume);
            });
        }
        if(CollectionUtils.isNotEmpty(dto.getEducationList())){
            dto.getEducationList().stream().forEach( education -> {
                education.setId(Identities.uuid());
                education.setDataId(fellowId);
                educationMapper.insert(education);
            });
        }
        baseMapper.insert(dto);
    }

    @Override
    public void editFellow(Fellow dto){
        if(CollectionUtils.isNotEmpty(dto.getResumeList())){
            resumeMapper.delete( new QueryWrapper<FellowResume>().eq("data_id", dto.getId()));
            dto.getResumeList().stream().forEach( resume -> {
                resume.setId(Identities.uuid());
                resume.setDataId(dto.getId());
                resumeMapper.insert(resume);
            });
        }
        if(CollectionUtils.isNotEmpty(dto.getEducationList())){
            educationMapper.delete( new QueryWrapper<FellowEducation>().eq("data_id", dto.getId()));
            dto.getEducationList().stream().forEach( education -> {
                education.setId(Identities.uuid());
                education.setDataId(dto.getId());
                educationMapper.insert(education);
            });
        }
        baseMapper.updateById(dto);
    }

    @Override
    public Fellow findById(String id){
        Fellow dto = baseMapper.selectById(id);
        if( null != dto ){
            dto.setEducationList( educationMapper.queryByDataId(id));
            dto.setResumeList( resumeMapper.queryByDataId(id));
        }
        return dto;
    }

    @Override
    public void removeFellow(String id){
        resumeMapper.delete( new QueryWrapper<FellowResume>().eq("data_id", id));
        educationMapper.delete( new QueryWrapper<FellowEducation>().eq("data_id", id));
        baseMapper.deleteById(id);
    }

    @Override
    public String findByZjhm(String zjhm){
        List<Fellow> list = baseMapper.selectList(new QueryWrapper<Fellow>().eq("sfzh", zjhm));
        if( null != list && list.size() > 0){
            return list.get(0).getId();
        }else{
            return null;
        }
    }


}

