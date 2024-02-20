package cn.sibetech.fellow.controller;

import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.core.util.Identities;
import cn.sibetech.fellow.domain.*;
import cn.sibetech.fellow.domain.dto.ContractQueryDto;
import cn.sibetech.fellow.domain.dto.FellowBranchQueryDto;
import cn.sibetech.fellow.domain.dto.FellowQueryDto;
import cn.sibetech.fellow.domain.validate.BranchSortValidate;
import cn.sibetech.fellow.domain.validate.FellowBranchValidate;
import cn.sibetech.fellow.service.BranchSortService;
import cn.sibetech.fellow.service.FellowBranchMemberService;
import cn.sibetech.fellow.service.FellowBranchService;
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
import java.util.List;
import java.util.Map;

import static cn.sibetech.core.util.SecurityConstants.*;


@Api(tags = "校友会")
@Slf4j
@RestController
@RequestMapping("/fellow_branch")
public class BranchController {

    @Autowired
    private FellowBranchService baseService;

    @Autowired
    private FellowBranchMemberService bMemberService;


    @ApiOperation(value = "管理")
    @PostMapping("/tree/{type}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<List<Map<String, Object>>> tree(@PathVariable String type) {
        try {
            List<Map<String, Object>> data = baseService.tree(type);
            return ResponseBean.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }


    @ApiOperation(value = "管理")
    @PostMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<FellowBranch>> page(@RequestBody @Valid FellowBranchQueryDto dto) {
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
    public ResponseBean<String> create(@RequestBody @Valid FellowBranch dto) {
        List<String> errors = FellowBranchValidate.validate(dto, true);
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
    public ResponseBean<String> update(@RequestBody @Valid FellowBranch dto) {
        List<String> errors = FellowBranchValidate.validate(dto, false);
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
    public ResponseBean<FellowBranch> detail(@RequestBody @Valid IdDto dto) {
        try {
            FellowBranch vo = baseService.getById(dto.getId());
            return ResponseBean.ok(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "校友会成员列表")
    @PostMapping("/page_member")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<Fellow>> pageMember(@RequestBody  @Valid FellowQueryDto dto) {
        if( StringUtils.isEmpty( dto.getBranchId() )){
            return ResponseBean.error("请选择需要查询的分会！");
        }
        try {
            IPage<Fellow> page = bMemberService.page(dto, dto.getBranchId());
            return ResponseBean.ok(page);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "添加校友")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/edit_member")
    public ResponseBean<String> editMember(@RequestBody @Valid FellowBranchMember dto) {
        if( StringUtils.isEmpty(dto.getBranchId()) || StringUtils.isEmpty(dto.getFellowId())){
            return ResponseBean.error("参数错误！");
        }
        try {
            if(!bMemberService.isFellowInBranch(dto.getBranchId(), dto.getFellowId())) {
                dto.setId(Identities.uuid());
                bMemberService.save(dto);
                return ResponseBean.ok("新增成功");
            }else{
                return ResponseBean.error("该校友已经添加！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "批量添加校友")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/batch_edit_member")
    public ResponseBean<String> batchEditMember(@RequestBody @Valid FellowBranchMember dto) {
        if( StringUtils.isEmpty(dto.getBranchId()) || CollectionUtils.isEmpty(dto.getFellowIdList())){
            return ResponseBean.error("参数错误！");
        }
        try {
            int count = bMemberService.addBatch(dto.getFellowIdList(), dto.getBranchId());
            return ResponseBean.ok("成功添加"+ count+ "位校友， 如有出入, 可能是选择的校友之前已经添加！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }


    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/remove_member")
    public ResponseBean<String> removeMember(@RequestBody @Valid FellowBranchMember dto) {
        if( StringUtils.isEmpty(dto.getId()) || StringUtils.isEmpty(dto.getBranchId())){
            return ResponseBean.error("参数错误！");
        }
        try {
            bMemberService.remove(dto.getId(), dto.getBranchId());
            return ResponseBean.okMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

}
