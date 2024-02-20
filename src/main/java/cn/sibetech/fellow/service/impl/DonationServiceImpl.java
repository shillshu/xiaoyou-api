package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.util.CryptUtils;
import cn.sibetech.core.util.Identities;
import cn.sibetech.core.util.MSWordUtil;
import cn.sibetech.core.util.VFSUtil;
import cn.sibetech.core.util.word.WordTemplateParam;
import cn.sibetech.core.util.word.WordUtil;
import cn.sibetech.fellow.domain.Donation;
import cn.sibetech.fellow.domain.dto.DonationQueryDto;
import cn.sibetech.fellow.mapper.DonationMapper;
import cn.sibetech.fellow.service.DonationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class DonationServiceImpl extends ServiceImpl<DonationMapper, Donation> implements DonationService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public IPage<Donation> queryPage(DonationQueryDto dto) {
        Page<Donation> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public IPage<Donation> pageNotZs(Donation dto, int current, int size) {
        Page<Donation> page = new Page<>(current, size);
        return baseMapper.queryPageNotZs(page, dto);
    }

    @Override
    public List<Donation> findList(Donation dto){
        return baseMapper.findList(dto);
    }


    @Override
    public Map getTjDonation(String itemId){
        return baseMapper.findTjList(itemId);
    }

    @Override
    public List<Map> findXyTjList(String itemId){
        return baseMapper.findXyTjList(itemId);
    }

    @Override
    public List<Map> getXyDonationList(String openId){
        return baseMapper.getXyDonationList(openId);
    }

    @Override
    public Donation findByNo(String no){
        QueryWrapper<Donation> wrapper= new QueryWrapper<>();
        wrapper.eq("order_no",no);
        List<Donation> list = baseMapper.selectList(wrapper);
        if( null != list && list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }

    public String getZsCode(){
        String ZS_CODE_PROFIX  = "HIT";
        String day = new SimpleDateFormat("yyyy").format(new Date());
        String key = "questioncode_" + day;
        int number = 1;
        Object t = redisTemplate.opsForValue().get(key);
        if( t == null ){
            String maxCode = baseMapper.queryMaxCode(ZS_CODE_PROFIX + day + "%");
            if(StringUtils.isNotEmpty(maxCode)){
                String maxNumber = maxCode.substring(maxCode.length() - 4);
                number = Integer.parseInt(maxNumber) + 1;
            }
        }else{
            number = (Integer)t + 1;
        }
        Set<String> keys = redisTemplate.keys( "questioncode_*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
        redisTemplate.opsForValue().set(key, number);

        String[] bl = {"00000","0000","000","00","0",""};
        int num =0;
        if(number/10==0){
            num =0;
        }else if(number/100==0){
            num =1;
        }else if(number/1000==0){
            num =2;
        }else if(number/10000==0){
            num =3;
        }else if(number/100000==0){
            num =4;
        }else{
            num =5;
        }
        String zsh = ZS_CODE_PROFIX + day +bl[num]+number;
        return zsh;
    }

    @Override
    public boolean validateJzInfo(String jzr, String jzsj){
        QueryWrapper<Donation> wrapper= new QueryWrapper<>();
        wrapper.eq("xm",jzr);
        wrapper.eq("jzsj",jzsj);
        return baseMapper.selectCount(wrapper) > 0;
    }

    public String getHashFilepath(String md5) {
        if (md5 == null || md5.length() < 4) {
            md5 = CryptUtils.md5(Identities.uuid());
        }

        StringBuffer path = new StringBuffer();
        path.append("/").append("zs").append("/").append(StringUtils.substring(md5, 0, 2)).append("/").append(StringUtils.substring(md5, md5.length() - 2));
        return path.toString();
    }


 /*   public String saveZs(HttpServletResponse response, String donationId, String dirPath, String templateName, String filename, Map<String, Object> params )throws IOException {
        dirPath = this.getHashFilepath(dirPath) + File.separator + dirPath;
        String ext = templateName.substring(templateName.lastIndexOf("."));
        WordIO.exportVfs(VFSUtil.getVFSPath(dirPath), templateName, filename, params);
        com.spire.doc.Document document = new com.spire.doc.Document();
        document.loadFromFile(VFSUtil.getVFSPath(dirPath)  + File.separator + filename + ext);
        String pdfFilePath = VFSUtil.getVFSPath(dirPath)  + File.separator + filename + ".pdf";
        document.saveToFile(pdfFilePath, FileFormat.PDF);
        *//*BufferedImage image = document.saveToImages(0, ImageType.Bitmap);
        File file = new File(VFSUtil.getVFSPath(dirPath)  + File.separator + filename + ".png");
        ImageIO.write(image, "PNG", file);*//*
        VFSUtil.removeFile(dirPath +  File.separator + filename + ext);
        return  dirPath + File.separator + filename + ".pdf";
    }*/

    public String saveImgZs(HttpServletResponse response, String donationId, String dirPath, String templateName, String filename, Map<String, Object> params ) throws Exception {
        WordTemplateParam param = new WordTemplateParam();
        param.setResponse(response);

        dirPath = this.getHashFilepath(dirPath) + File.separator + dirPath;
        String path = "/jzzs/" + dirPath + "/";
        File file = VFSUtil.getFileIsNotExistCreate( path);

        param.setTemplatePath(VFSUtil.getVFSPath(templateName));
        param.setPath(file.getPath()  + "/");
        param.setFilename(filename);
        param.setData(params);
        WordUtil.exportVfsFile(param);
        MSWordUtil.covert2PNG(file.getPath() + "/" + donationId + ".docx", file.getPath(), donationId);
        return  path+donationId+".png";
    }

    @Override
    public String savePdfZs(HttpServletResponse response, String donationId,  String dirPath, String templateName, String filename, Map<String, Object> params )throws Exception {
        WordTemplateParam param = new WordTemplateParam();
        param.setResponse(response);

        dirPath = this.getHashFilepath(dirPath) + File.separator + dirPath;
        String path = "/jzzs/" + dirPath + "/";
        File file = VFSUtil.getFileIsNotExistCreate( path);

        param.setTemplatePath(VFSUtil.getVFSPath(templateName));
        param.setPath(file.getPath()  + "/");
        param.setFilename(filename);
        param.setData(params);
        WordUtil.exportVfsFile(param);
        MSWordUtil.covert2PDF(file.getPath() + "/" + donationId + ".docx", file.getPath(), donationId);
        return  path+donationId+".pdf";
    }

}

