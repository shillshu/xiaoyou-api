package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.dto.FellowQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface FellowService extends IService<Fellow> {
    public IPage<Fellow> queryPage(FellowQueryDto dto);

    public List<Fellow> findList(Fellow dto);

    public List<Fellow> findExportList(Fellow dto);

    List<Fellow> findByXmAndCsrq(String xm, String csrq);


    List<Fellow> findByXmAndRxsj(String xm, String rxnf, String bynf);

    void addFellow(Fellow dto);

    void editFellow(Fellow dto);

    Fellow findById(String id);

    void removeFellow(String id);

    String findByZjhm(String zjhm);
}
