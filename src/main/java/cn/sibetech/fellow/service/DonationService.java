package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.Donation;
import cn.sibetech.fellow.domain.dto.DonationQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public interface DonationService extends IService<Donation> {
    public IPage<Donation> queryPage(DonationQueryDto dto);

    public IPage<Donation> pageNotZs(Donation dto, int current, int size);

    public List<Donation> findList(Donation dto);

    public Map getTjDonation(String itemId);

    List<Map> findXyTjList(String itemId);

    public Donation findByNo(String no);

    List<Map> getXyDonationList(String openId);

    public boolean validateJzInfo(String jzr, String jzsj);

    public String getZsCode();

    String saveImgZs(HttpServletResponse response, String donationId, String dirPath, String templateName, String filename, Map<String, Object> params )throws Exception;

    String savePdfZs(HttpServletResponse response, String donationId, String dirPath, String templateName, String filename, Map<String, Object> params )throws Exception;



}
