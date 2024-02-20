package cn.sibetech.fellow.controller;

import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.core.util.VFSUtil;
import cn.sibetech.fellow.domain.FileDown;
import cn.sibetech.fellow.domain.dto.FileDownQueryDto;
import cn.sibetech.fellow.service.FileDownService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static cn.sibetech.core.util.SecurityConstants.*;


@Api(tags = "下载")
@Slf4j
@RestController
@RequestMapping("/file_down")
public class FileDownController {

    @Autowired
    private FileDownService baseService;


    @ApiOperation(value = "管理")
    @PostMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<FileDown>> page(@RequestBody @Valid FileDownQueryDto dto) {
        try {
            return ResponseBean.ok(baseService.queryPage(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/delete")
    public ResponseBean<String> delete(@RequestBody @Valid IdDto dto) {
        try {
            if (StringUtils.isEmpty(dto.getId())) {
                return ResponseBean.error("参数错误,id不能为空");
            }
            baseService.removeById(dto.getId());
            return ResponseBean.okMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "获取基本信息")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/detail")
    public ResponseBean<FileDown> detail(@RequestBody @Valid IdDto dto) {
        try {
            FileDown vo = baseService.getById(dto.getId());
            return ResponseBean.ok(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }


    @RequestMapping("/download")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(StringUtils.isNotEmpty(id)){
            FileDown dto = baseService.getById(id);
           /* dto.setCount(dto.getCount()+1);
            fileJobService.updateById(dto);*/
            String fileName = dto.getName();
           /* String exportFileName = fileName.substring(0, fileName.indexOf("."))
                    +  "_" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
                    + fileName.substring(fileName.indexOf("."));*/
            VFSUtil.downloadFile(response, request, dto.getPath(), fileName);

        }
    }

}
