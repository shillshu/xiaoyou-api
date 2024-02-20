package cn.sibetech.fellow.controller;

import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.fellow.domain.ActivityItem;
import cn.sibetech.fellow.domain.Contact;
import cn.sibetech.fellow.domain.dto.ContractQueryDto;
import cn.sibetech.fellow.domain.validate.ActivityItemValidate;
import cn.sibetech.fellow.domain.validate.ContactValidate;
import cn.sibetech.fellow.service.ContactFellowService;
import cn.sibetech.fellow.service.ContactService;
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
import java.util.ArrayList;
import java.util.List;

import static cn.sibetech.core.util.SecurityConstants.*;


@Api(tags = "校友联络")
@Slf4j
@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService baseService;

    @Autowired
    private ContactFellowService cFellowService;

    @ApiOperation(value = "管理")
    @PostMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<Contact>> page(@RequestBody @Valid ContractQueryDto dto) {
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
    public ResponseBean<String> create(@RequestBody @Valid Contact dto) {
        List<String> errors = ContactValidate.validate(dto, true);
        if(CollectionUtils.isNotEmpty(errors)){
            return ResponseBean.error(StringUtils.join(errors));
        }
        try {
            baseService.add(dto);
            return ResponseBean.okMsg("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "修改")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/update")
    public ResponseBean<String> update(@RequestBody @Valid Contact dto) {
        List<String> errors = ContactValidate.validate(dto, false);
        if(CollectionUtils.isNotEmpty(errors)){
            return ResponseBean.error(StringUtils.join(errors));
        }
        try {
            baseService.edit(dto);
            return ResponseBean.okMsg("修改成功");
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
            baseService.remove(dto.getId());
            return ResponseBean.okMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "获取基本信息")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/detail")
    public ResponseBean<Contact> detail(@RequestBody @Valid IdDto dto) {
        try {
            Contact vo = baseService.getById(dto.getId());
            if( null != vo){
                vo.setFellowList(cFellowService.findByContactId(dto.getId()));
            }
            return ResponseBean.ok(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }



}
