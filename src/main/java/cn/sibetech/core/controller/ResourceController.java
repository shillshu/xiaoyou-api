package cn.sibetech.core.controller;

import cn.sibetech.core.domain.ShiroUser;
import cn.sibetech.core.domain.dto.FileDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.core.service.ResourceService;
import cn.sibetech.core.service.SecurityService;
import cn.sibetech.core.util.Identities;
import cn.sibetech.core.util.VFSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

import static cn.sibetech.core.util.SecurityConstants.*;


@Api(tags = "上传")
@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private SecurityService securityService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @RequestMapping("/uploadFileFake")
    public ResponseBean<String> uploadFileFake() throws Exception {
        return ResponseBean.okMsg("");
    }
    @ApiOperation(value = "上传文件")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/uploadFile")
    public ResponseBean<FileDto> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            if(file.getSize()>10*1024*1024){
                return ResponseBean.error("请上传不大于10M的文件");
            }
            FileDto fileDto = resourceService.upload(file);
            return ResponseBean.ok(fileDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }
    @ApiOperation(value = "下载文件", hidden = true)
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @RequestMapping("/downloadFile")
    public void downloadFile(String path, HttpServletRequest request,HttpServletResponse response) throws Exception{
        String filename = path.substring(path.lastIndexOf("/") + 1);
        VFSUtil.downloadFile(response, request, path, filename);
    }


    @RequestMapping("/showImg/{userKey}")
    public void showImg(@PathVariable("userKey") String userKey, HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
        ShiroUser user = securityService.getLoginUser(userKey);
        if(user!=null){
            VFSUtil.showVFSImage(request, response, path);
        }
    }

    @RequestMapping("/preview/{userKey}")
    public void preview(@PathVariable("userKey") String userKey, String path, HttpServletRequest request, HttpServletResponse response) throws Exception{
        ShiroUser shiroUser = securityService.getLoginUser(userKey);
        if(shiroUser!=null) {
            if(StringUtils.isNotEmpty(path)) {
                VFSUtil.previewPdf(response, request, path, Identities.uuid()+".pdf");
            }
        }
    }

    @ApiOperation(value = "下载文件", hidden = true)
    @RequestMapping("/downloadExport/{userKey}/{id}")
    public void downloadExport(@PathVariable("userKey") String userKey,@PathVariable("id") String id, HttpServletRequest request,HttpServletResponse response) throws Exception{
        ShiroUser shiroUser = securityService.getLoginUser(userKey);
        if(shiroUser!=null) {
            String filepath = (String) redisTemplate.opsForValue().get("downloadExport:"+id);
            if(StringUtils.isNotEmpty(filepath)){
                InputStream reader = null;
                OutputStream out = null;
                String filename = filepath.substring(filepath.lastIndexOf(File.separator)+1);
                try {
                    File file = new File(filepath);
                    reader = new FileInputStream(file);
                    byte[] buf = new byte[1024];
                    int len = 0;
                    response.addHeader("Content-Length", "" + file.length());
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition", "attachment;filename=" + filename);
                    out = response.getOutputStream();
                    while ((len = reader.read(buf)) != -1) {
                        out.write(buf, 0, len);
                    }
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception ex) {
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        }
    }

}
