package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.FellowEducation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface FellowEducationService extends IService<FellowEducation> {
    List<FellowEducation> queryList(FellowEducation edu);

    List<FellowEducation> queryListByFellowId(String fellowId);

    Long queryCountFellowId(String fellowId);

    List<FellowEducation> queryByFellowInfo(String fellowId,String xm, String rxnf, String bynf);

    String findByXmAndXh(String xm, String xh);

    String findByFellowIdAndXh(String xm, String xh);
}
