package cn.sibetech.fellow.controller;

import cn.sibetech.core.domain.dto.IdsDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.core.util.Identities;
import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.FellowVipMember;
import cn.sibetech.fellow.domain.GroupMember;
import cn.sibetech.fellow.domain.dto.FellowAuthInfoQueryDto;
import cn.sibetech.fellow.domain.dto.FellowQueryDto;
import cn.sibetech.fellow.service.FellowVipMemberService;
import cn.sibetech.fellow.service.GroupMemberService;
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

import static cn.sibetech.core.util.SecurityConstants.*;


@Api(tags = "分组人员管理")
@Slf4j
@RestController
@RequestMapping("/vip_member")
public class GroupMemberController {

    @Autowired
    private GroupMemberService baseService;


    @PostMapping("/page_member")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<FellowAuthInfo>> pageMember(@RequestBody  @Valid FellowAuthInfoQueryDto dto) {
        if( CollectionUtils.isEmpty( dto.getGroupIdList() )){
            return ResponseBean.error("请选择需要查询的分组！");
        }
        try {
            IPage<FellowAuthInfo> page = baseService.queryPage(dto, dto.getGroupIdList());
            return ResponseBean.ok(page);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "添加校友")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/edit_member")
    public ResponseBean<String> editMember(@RequestBody @Valid GroupMember dto) {
        if( StringUtils.isEmpty(dto.getGroupId()) || StringUtils.isEmpty(dto.getFellowId())){
            return ResponseBean.error("参数错误！");
        }
        try {
            if(!baseService.isFellowInKind(dto.getGroupId(), dto.getFellowId())) {
                dto.setId(Identities.uuid());
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
    public ResponseBean<String> batchEditMember(@RequestBody @Valid GroupMember dto) {
        if( StringUtils.isEmpty(dto.getGroupId()) || CollectionUtils.isEmpty(dto.getFellowIdList())){
            return ResponseBean.error("参数错误！");
        }
        try {
            int count = baseService.addBatch(dto.getFellowIdList(), dto.getGroupId());
            return ResponseBean.ok("成功添加"+ count+ "位校友， 如有出入, 可能是选择的校友之前已经添加！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }


    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/remove_member")
    public ResponseBean<String> removeMember(@RequestBody @Valid GroupMember dto) {
        if( StringUtils.isEmpty(dto.getId()) || StringUtils.isEmpty(dto.getGroupId())){
            return ResponseBean.error("参数错误！");
        }
        try {
            baseService.remove(dto.getId(), dto.getGroupId());
            return ResponseBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "查看选择的分组的人数")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/count_member")
    public ResponseBean<Long> countMember(@RequestBody @Valid IdsDto dto) {
        try {
            Long count = baseService.count(dto.getIds());
            return ResponseBean.ok(count);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

}
