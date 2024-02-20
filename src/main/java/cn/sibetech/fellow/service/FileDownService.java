package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.FileDown;
import cn.sibetech.fellow.domain.dto.FileDownQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface FileDownService extends IService<FileDown> {
    public IPage<FileDown> queryPage(FileDownQueryDto dto);

    public List<FileDown> findList(FileDownQueryDto dto);

    FileDown add(FileDown dto);

    FileDown addWait(String lb, String name, String path);

    void finish(String id);

    void error(String id);

    void edit(FileDown dto);

    void remove(String[] ids);

    void disable(String[] ids);

    void enable(String[] ids);

}
