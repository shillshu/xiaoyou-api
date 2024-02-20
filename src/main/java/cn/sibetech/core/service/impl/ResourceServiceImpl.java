package cn.sibetech.core.service.impl;

import cn.sibetech.core.domain.dto.FileDto;
import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.service.ResourceService;
import cn.sibetech.core.util.FileUtils;
import cn.sibetech.core.util.Identities;
import cn.sibetech.core.util.VFSUtil;
import cn.sibetech.core.util.excel.ExcelTableParam;
import cn.sibetech.core.util.excel.ExcelTemplateParam;
import cn.sibetech.core.util.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public FileDto upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String suffix = null;
        if (index >= 0) {
            suffix = originalFilename.substring(index + 1);
        }
        try {
            String md5 = Identities.uuid();
            String size ="0kb";
            String filepath = FileUtils.getHashFilepath(md5) + "/" + md5 + (StringUtils.isEmpty(suffix) ? "" : "." + suffix.toLowerCase());
            InputStream is = file.getInputStream();
            size = is.available()/1024+"kb";
            VFSUtil.saveFile(is, filepath);
            FileDto dto = new FileDto();
            dto.setUrl(filepath);
            dto.setName(originalFilename);
            dto.setFileSize(size);
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String export(ExcelTableParam excelTableParam) {
        String md5 = Identities.getUUID8();
        String rootPath = FileUtils.getHashFilepath(md5);
        File rootFile = VFSUtil.getFileIsNotExistCreate(rootPath);
        excelTableParam.setFilePath(rootFile.getPath() + File.separator + md5 + ExcelTypeEnum.XLSX.getValue());
        ExcelUtil.exportFile(excelTableParam);
        redisTemplate.opsForValue().set("downloadExport:"+md5, excelTableParam.getFilePath(), 30, TimeUnit.MINUTES);
        return "/resource_show/downloadExport/"+ SecurityContextHolder.getCurrentUser().getUserKey()+"/"+md5;
    }

    @Override
    public String exportByTemplate(ExcelTemplateParam param) {
        String md5 = Identities.getUUID8();
        String rootPath = FileUtils.getHashFilepath(md5);
        File rootFile = VFSUtil.getFileIsNotExistCreate(rootPath);
        param.setFilePath(rootFile.getPath() + File.separator + md5 + ExcelTypeEnum.XLSX.getValue());
        ExcelUtil.exportFileForTemplate(param);
        redisTemplate.opsForValue().set("downloadExport:"+md5, param.getFilePath(), 30, TimeUnit.MINUTES);
        return "/resource_show/downloadExport/"+ SecurityContextHolder.getCurrentUser().getUserKey()+"/"+md5;
    }

    @Override
    public String exportForFile(String id, String path) {
        redisTemplate.opsForValue().set("downloadExport:"+id, path, 30, TimeUnit.MINUTES);
        return "/resource_show/downloadExport/"+ SecurityContextHolder.getCurrentUser().getUserKey()+"/"+id;
    }

    @Override
    public String exportObject(Class head, Collection<?> data, String sheetName) {
        String md5 = Identities.getUUID8();
        String rootPath = FileUtils.getHashFilepath(md5);
        File rootFile = VFSUtil.getFileIsNotExistCreate(rootPath);
        String filePath = rootFile.getPath() + File.separator + md5 + ExcelTypeEnum.XLSX.getValue();
        EasyExcel.write(filePath, head).autoCloseStream(Boolean.TRUE).sheet(sheetName).doWrite(data);
        redisTemplate.opsForValue().set("downloadExport:"+md5, filePath, 30, TimeUnit.MINUTES);
        return "/resource_show/downloadExport/"+ SecurityContextHolder.getCurrentUser().getUserKey()+"/"+md5;
    }

}
