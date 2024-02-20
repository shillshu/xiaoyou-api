package cn.sibetech.fellow.controller;

import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.core.util.Identities;
import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.FellowBranch;
import cn.sibetech.fellow.domain.FellowBranchMember;
import cn.sibetech.fellow.domain.FellowVipMember;
import cn.sibetech.fellow.domain.dto.FellowBranchQueryDto;
import cn.sibetech.fellow.domain.dto.FellowQueryDto;
import cn.sibetech.fellow.domain.validate.FellowBranchValidate;
import cn.sibetech.fellow.service.FellowBranchMemberService;
import cn.sibetech.fellow.service.FellowBranchService;
import cn.sibetech.fellow.service.FellowVipMemberService;
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


@Api(tags = "重点校友管理")
@Slf4j
@RestController
@RequestMapping("/vip_member")
public class VipMemberController {

    @Autowired
    private FellowVipMemberService baseService;


    @PostMapping("/page_member")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<Fellow>> pageMember(@RequestBody  @Valid FellowQueryDto dto) {
        if( StringUtils.isEmpty( dto.getKindId() )){
            return ResponseBean.error("请选择需要查询的分类！");
        }
        try {
            IPage<Fellow> page = baseService.page(dto, dto.getKindId());
            return ResponseBean.ok(page);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "添加校友")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/edit_member")
    public ResponseBean<String> editMember(@RequestBody @Valid FellowVipMember dto) {
        if( StringUtils.isEmpty(dto.getKindId()) || StringUtils.isEmpty(dto.getFellowId())){
            return ResponseBean.error("参数错误！");
        }
        try {
            if(!baseService.isFellowInKind(dto.getKindId(), dto.getFellowId())) {
                dto.setId(Identities.uuid());
                dto.setFlag("");
                baseService.save(dto);
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
    public ResponseBean<String> batchEditMember(@RequestBody @Valid FellowVipMember dto) {
        if( StringUtils.isEmpty(dto.getKindId()) || CollectionUtils.isEmpty(dto.getFellowIdList())){
            return ResponseBean.error("参数错误！");
        }
        try {
            int count = baseService.addBatch(dto.getFellowIdList(), dto.getKindId());
            return ResponseBean.ok("成功添加"+ count+ "位校友， 如有出入, 可能是选择的校友之前已经添加！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }


    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/remove_member")
    public ResponseBean<String> removeMember(@RequestBody @Valid FellowVipMember dto) {
        if( StringUtils.isEmpty(dto.getId()) || StringUtils.isEmpty(dto.getKindId())){
            return ResponseBean.error("参数错误！");
        }
        try {
            baseService.remove(dto.getId(), dto.getKindId());
            return ResponseBean.okMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

}
