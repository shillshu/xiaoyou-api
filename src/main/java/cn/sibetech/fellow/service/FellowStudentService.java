package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.FellowStudent;
import cn.sibetech.fellow.domain.dto.FellowStudentQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface FellowStudentService extends IService<FellowStudent> {
    public IPage<FellowStudent> queryPage(FellowStudentQueryDto dto);

    public List<FellowStudent> findList(FellowStudent dto);

    boolean syncOneStudent(String id);

    int syncAllStudent(FellowStudent dto);

}
