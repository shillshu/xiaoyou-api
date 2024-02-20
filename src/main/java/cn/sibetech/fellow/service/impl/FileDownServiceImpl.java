package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.util.Identities;
import cn.sibetech.core.util.VFSUtil;
import cn.sibetech.fellow.domain.FileDown;
import cn.sibetech.fellow.domain.dto.FileDownQueryDto;
import cn.sibetech.fellow.mapper.FileDownMapper;
import cn.sibetech.fellow.service.FileDownService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FileDownServiceImpl extends ServiceImpl<FileDownMapper, FileDown> implements FileDownService {
    @Override
    public IPage<FileDown> queryPage(FileDownQueryDto dto) {
        Page<FileDown> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public List<FileDown> findList(FileDownQueryDto dto){
        return baseMapper.queryPage(dto);
    }

    @Override
    public FileDown add(FileDown dto) {
        if(StringUtils.isEmpty(dto.getId())){
            dto.setId(Identities.uuid());
        }
        dto.setCjrId(SecurityContextHolder.getCurrentUser().getUserId());
        dto.setCjrName(SecurityContextHolder.getCurrentUser().getName());
        baseMapper.insert(dto);
        return dto;
    }

    @Override
    public FileDown addWait(String lb, String name, String path){
        FileDown dto = new FileDown();
        dto.setStatus("0");
        dto.setLb(lb);
        dto.setName(name);
        dto.setPath(path);
        return this.add(dto);
    }

    @Override
    public void finish(String id){
        FileDown dto = new FileDown();
        dto.setId(id);
        dto.setStatus("1");
        this.edit(dto);
    }

    @Override
    public void error(String id){
        FileDown dto = new FileDown();
        dto.setId(id);
        dto.setStatus("500");
        this.edit(dto);
    }

    @Override
    public void edit(FileDown dto) {
        baseMapper.updateById(dto);
    }

    @Override
    public void remove(String[] ids){
        if(ArrayUtils.isNotEmpty(ids)){
            for(String id:ids){
                FileDown file = baseMapper.selectById(id);
                if( null != file  && StringUtils.isNotEmpty(file.getPath())){
                    VFSUtil.removeFile(file.getPath());
                }
                baseMapper.deleteById(id);
            }
        }
    }

    @Override
    public void disable(String[] ids) {
        if(ArrayUtils.isNotEmpty(ids)){
            for(String id:ids){
                FileDown dto = baseMapper.selectById(id);
                dto.setStatus("0");
                this.edit(dto);
            }
        }
    }

    @Override
    public void enable(String[] ids) {
        if(ArrayUtils.isNotEmpty(ids)){
            for(String id:ids){
                FileDown dto = baseMapper.selectById(id);
                dto.setStatus("1");
                this.edit(dto);
            }
        }
    }
}

