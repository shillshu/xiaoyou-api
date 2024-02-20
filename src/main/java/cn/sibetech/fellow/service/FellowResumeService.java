package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.FellowResume;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface FellowResumeService extends IService<FellowResume> {
    List<FellowResume> queryList(FellowResume resume);

    List<FellowResume> queryListByFellowId(String fellowId);

    Long queryCountFellowId(String fellowId);
}
