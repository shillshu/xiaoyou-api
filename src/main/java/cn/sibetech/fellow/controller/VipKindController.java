package cn.sibetech.fellow.controller;

import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.fellow.domain.Activity;
import cn.sibetech.fellow.domain.BranchSort;
import cn.sibetech.fellow.domain.Group;
import cn.sibetech.fellow.domain.VipKind;
import cn.sibetech.fellow.domain.dto.ActivityQueryDto;
import cn.sibetech.fellow.domain.dto.VipKindQueryDto;
import cn.sibetech.fellow.domain.validate.BranchSortValidate;
import cn.sibetech.fellow.domain.validate.VipKindValidate;
import cn.sibetech.fellow.service.BranchSortService;
import cn.sibetech.fellow.service.VipKindService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static cn.sibetech.core.util.SecurityConstants.*;


@Api(tags = "重点校友分类")
@Slf4j
@RestController
@RequestMapping("/vip_kind")
public class VipKindController {

    @Autowired
    private VipKindService baseService;


    @ApiOperation(value = "管理")
    @PostMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<VipKind>> page(@RequestBody @Valid VipKindQueryDto dto) {
        try {
            return ResponseBean.ok(baseService.queryPage(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "管理")
    @PostMapping("/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<List<Map>> tree() {
        try {
            List<Map> data = baseService.tree();
            return ResponseBean.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "新增")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/create")
    public ResponseBean<String> create(@RequestBody @Valid VipKind dto) {
        List<String> errors = VipKindValidate.validate(dto, true);
        if(CollectionUtils.isNotEmpty(errors)){
            return ResponseBean.error(StringUtils.join(errors));
        }
        try {
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
    public ResponseBean<String> update(@RequestBody @Valid VipKind dto) {
        List<String> errors = VipKindValidate.validate(dto, false);
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
    public ResponseBean<VipKind> detail(@RequestBody @Valid IdDto dto) {
        try {
            VipKind vo = baseService.getById(dto.getId());
            return ResponseBean.ok(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }


}
