package cn.sibetech.fellow.controller;

import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.FellowStudent;
import cn.sibetech.fellow.domain.dto.FellowQueryDto;
import cn.sibetech.fellow.domain.dto.FellowStudentQueryDto;
import cn.sibetech.fellow.domain.validate.FellowValidate;
import cn.sibetech.fellow.service.FellowService;
import cn.sibetech.fellow.service.FellowStudentService;
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

import javax.validation.Valid;
import java.util.List;

import static cn.sibetech.core.util.SecurityConstants.*;


@Api(tags = "校友信息同步")
@Slf4j
@RestController
@RequestMapping("/fellow_student")
public class FellowStudentController {

    @Autowired
    private FellowStudentService baseService;




    @ApiOperation(value = "管理")
    @PostMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<FellowStudent>> page(@RequestBody @Valid FellowStudentQueryDto dto) {
        try {
            return ResponseBean.ok(baseService.queryPage(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "新增")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/sync_student_all")
    public ResponseBean<String> syncAllStudent(@RequestBody @Valid FellowStudent dto) {
        try {
            dto.setStatus("0");
            int count = baseService.syncAllStudent(dto);
            return ResponseBean.ok("成功同步更新" + count + "条校友信息！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error("同步失败！");
        }
    }


    @ApiOperation(value = "新增")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/sync_student_one")
    public ResponseBean<String> syncOneStudent(@RequestBody @Valid IdDto dto) {
        try {
            boolean flag = baseService.syncOneStudent(dto.getId());
            return ResponseBean.ok("同步成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error("同步失败！");
        }
    }


}
