package cn.sibetech.fellow.controller;

import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.core.util.Identities;
import cn.sibetech.core.util.excel.ExcelTemplateParam;
import cn.sibetech.core.util.excel.ExcelUtil;
import cn.sibetech.core.util.excel.RowModel;
import cn.sibetech.fellow.domain.ActivityFellow;
import cn.sibetech.fellow.domain.Sign;
import cn.sibetech.fellow.domain.SignFellow;
import cn.sibetech.fellow.domain.dto.SignFellowQueryDto;
import cn.sibetech.fellow.domain.dto.SignQueryDto;
import cn.sibetech.fellow.domain.validate.SignValidate;
import cn.sibetech.fellow.service.ActivityFellowService;
import cn.sibetech.fellow.service.SignFellowService;
import cn.sibetech.fellow.service.SignService;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static cn.sibetech.core.util.SecurityConstants.*;


@Api(tags = "签到管理")
@Slf4j
@RestController
@RequestMapping("/sign")
public class SignController {

    @Autowired
    private SignService baseService;

    @Autowired
    private SignFellowService sFellowService;

    @Autowired
    private ActivityFellowService aFellowService;


    @ApiOperation(value = "管理")
    @PostMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<Sign>> page(@RequestBody @Valid SignQueryDto dto) {
        try {
            return ResponseBean.ok(baseService.queryPage(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }


    @ApiOperation(value = "新增")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/create")
    public ResponseBean<String> create(@RequestBody @Valid Sign dto) {
        List<String> errors = SignValidate.validate(dto, true);
        if(CollectionUtils.isNotEmpty(errors)){
            return ResponseBean.error(StringUtils.join(errors));
        }
        try {
            if(StringUtils.isNotEmpty(dto.getActivityId())){
                long count = aFellowService.count(dto.getActivityId());
                dto.setJhqdrs((int)count);
            }
            baseService.save(dto);
            return ResponseBean.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "修改")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/update")
    public ResponseBean<String> update(@RequestBody @Valid Sign dto) {
        List<String> errors = SignValidate.validate(dto, false);
        if(CollectionUtils.isNotEmpty(errors)){
            return ResponseBean.error(StringUtils.join(errors));
        }
        try {
            baseService.updateById(dto);
            return ResponseBean.ok("修改成功");
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
    public ResponseBean<Sign> detail(@RequestBody @Valid IdDto dto) {
        try {
            Sign vo = baseService.getById(dto.getId());
            return ResponseBean.ok(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "修改状态")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/edit_status")
    public ResponseBean<String> editStatus(@RequestBody @Valid Sign dto) {
        if(StringUtils.isEmpty(dto.getId()) || StringUtils.isEmpty(dto.getStatus())){
            return ResponseBean.error("参数错误！");
        }
        try {
            baseService.editStatus(dto.getId(), dto.getStatus());
            return ResponseBean.ok("结束成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "签到人员")
    @PostMapping("/sign_fellow_page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<SignFellow>> signFellowPage(@RequestBody @Valid SignFellowQueryDto dto) {
        try {
            if(StringUtils.isNotEmpty(dto.getType()) && "3".equals(dto.getType())){
                IPage<SignFellow> page = sFellowService.wqdPage(dto);
                return ResponseBean.ok(page);
            }else {
                dto.setJhStatus(dto.getType());
                IPage<SignFellow> page = sFellowService.page(dto);
                return ResponseBean.ok(page);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "删除签到状态")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/remove_sign_fellow")
    public ResponseBean<String> removeSignFellow(@RequestBody @Valid IdDto dto) {
        try {
            SignFellow signDto = new SignFellow();
            signDto.setId(dto.getId());
            signDto.setStatus("2");
            sFellowService.updateById(signDto);
            return ResponseBean.ok("取消成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "添加签到状态")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/add_sign_fellow")
    public ResponseBean<String> addSignFellow(@RequestBody @Valid SignFellow dto) {
        try {
            dto.setId(Identities.uuid());
            dto.setStatus("1");
            sFellowService.save(dto);
            return ResponseBean.ok("签到成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @RequestMapping("/export_sign_fellow")
    public void exportSignFellow(HttpServletResponse response, @RequestBody SignFellowQueryDto dto) throws Exception {
        List dataList;
        if(StringUtils.isNotEmpty(dto.getType()) && "3".equals(dto.getType())){
            dataList = sFellowService.wqdList(dto);
        }else {
            dto.setJhStatus(dto.getType());
            dataList = sFellowService.list(dto);
        }
        ExcelTemplateParam param = new ExcelTemplateParam();
        param.setTemplatePath("/static/excel/sign_fellow.xlsx");
        param.setFilename("参加活动人员信息");
        param.setResponse(response);
        List<RowModel> data = new ArrayList<>();
        RowModel thirdRow = new RowModel();
        thirdRow.setName("list");
        thirdRow.setData(dataList);
        data.add(thirdRow);
        param.setData(data);
        try {
            ExcelUtil.export2007(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
