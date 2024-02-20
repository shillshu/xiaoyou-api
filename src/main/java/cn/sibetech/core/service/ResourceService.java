package cn.sibetech.core.service;

import cn.sibetech.core.domain.dto.FileDto;
import cn.sibetech.core.util.excel.ExcelTableParam;
import cn.sibetech.core.util.excel.ExcelTemplateParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface ResourceService {
    FileDto upload(MultipartFile file);

    String export(ExcelTableParam param);

    String exportByTemplate(ExcelTemplateParam param);

    String exportForFile(String id, String path);

    String exportObject(Class head, Collection<?> data, String sheetName);
}
