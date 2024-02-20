package cn.sibetech.fellow.controller;

import cn.sibetech.core.domain.BizField;
import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.domain.dto.IdsDto;
import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.core.service.BizFieldService;
import cn.sibetech.core.util.DateUtils;
import cn.sibetech.core.util.excel.ExcelStUtil;
import cn.sibetech.fellow.domain.ActivityItem;
import cn.sibetech.fellow.domain.Contact;
import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.FellowAuthjg;
import cn.sibetech.fellow.domain.dto.ContractQueryDto;
import cn.sibetech.fellow.domain.dto.FellowAuthInfoQueryDto;
import cn.sibetech.fellow.domain.validate.ActivityItemValidate;
import cn.sibetech.fellow.domain.validate.ContactValidate;
import cn.sibetech.fellow.service.ContactFellowService;
import cn.sibetech.fellow.service.ContactService;
import cn.sibetech.fellow.service.FellowAuthInfoService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.sibetech.core.util.SecurityConstants.*;


@Api(tags = "校友联络")
@Slf4j
@RestController
@RequestMapping("/fellow_auth_info")
public class FellowAuthInfoController {

    @Autowired
    private FellowAuthInfoService baseService;

    @Autowired
    private FellowBranchService branchService;

    @Autowired
    private BizFieldService bFieldService;

    private static final String bizId = "xy_fellow_authinfo";

    @ApiOperation(value = "管理")
    @PostMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<FellowAuthInfo>> page(@RequestBody @Valid FellowAuthInfoQueryDto dto) {
        try {
            return ResponseBean.ok(baseService.queryPage(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "管理")
    @PostMapping("/fellow_info_list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<List<FellowAuthInfo>> fellowInfoList(@RequestBody @Valid IdDto dto) {
        try {
            List<FellowAuthInfo> dataList = baseService.findByFellowId(dto.getId());
            return ResponseBean.ok(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }


    /*人工审核时  查找同名的 校友*/
    @ApiOperation(value = "查找同名校友")
    @PostMapping("/fellow_list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<List<Map>> getFellowList(@RequestBody FellowAuthInfo dto) {
        try {
            List<Map> dataList = baseService.getFellowList(dto.getXm(), dto.getXy(), dto.getRxnf(), dto.getBynf());
            return ResponseBean.ok(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    /*人工审核时*/
    @ApiOperation(value = "人工审核时")
    @PostMapping("/approve")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<String> approve(@RequestBody FellowAuthjg dto) {
        FellowAuthInfo info = dto.getInfo();
        if( null == info || StringUtils.isEmpty(info.getId())){
            return ResponseBean.error("未找到审批数据！");
        }
        try {
            info.setShrId(SecurityContextHolder.getCurrentUser().getUserId());
            info.setShsj(DateUtils.getCurrentDateTime());
            if (StringUtils.isNotEmpty(dto.getSpjg()) && "1".equals(dto.getSpjg())) {
                if (StringUtils.isNotEmpty(dto.getFellowId())) {/* 选择 已存在校友*/
                    /*需要解绑*/
                    baseService.unBind(dto.getFellowId());
                    /* 更新bind  更新authinfo  生成authjg 绑定分会 更新校友相关信息*/
                    baseService.approveEditFellow(info, dto.getFellowId(), dto.getSpjg(), dto.getSpyj());
                } else {/* 未选中校友  需要新增一条校友信息*/
                    /*更新bind  更新authinfo  生成authjg 绑定分会 新增校友 及其相关信息*/
                    baseService.approveAddFellow(info, dto.getSpjg(), dto.getSpyj());
                }
            } else {
                baseService.noPassFellow(info, dto.getSpjg(), dto.getSpyj());/*审批不通过*/
            }
            return ResponseBean.ok("审核成功！");
        }catch(Exception e){
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }


    @ApiOperation(value = "修改")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/update")
    public ResponseBean<String> update(@RequestBody @Valid FellowAuthInfo dto) {
        try {
            baseService.edit(dto);
            return ResponseBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "获取基本信息")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/detail")
    public ResponseBean<FellowAuthInfo> detail(@RequestBody @Valid IdDto dto) {
        try {
            FellowAuthInfo vo = baseService.getById(dto.getId());
            return ResponseBean.ok(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "获取基本信息")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/detail_fellow")
    public ResponseBean<FellowAuthInfo> detailByFellowId(@RequestBody @Valid IdDto dto) {
        try {
            List<FellowAuthInfo> list = baseService.findByFellowId(dto.getId());
            if( CollectionUtils.isNotEmpty(list)){
                return ResponseBean.ok(list.get(0));
            }else{
                return ResponseBean.error("未找到校友信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @RequestMapping("/edit_xm")
    @ResponseBody
    public ResponseBean<String> editXm(@RequestBody @Valid FellowAuthInfo dto){
        if( StringUtils.isEmpty(dto.getId()) || StringUtils.isEmpty(dto.getXm())){
            return ResponseBean.error("参数错误！");
        }
        try {
            FellowAuthInfo authinfo = new FellowAuthInfo();
            authinfo.setId(dto.getId());
            authinfo.setXm(dto.getXm());
            baseService.edit(dto);
            return ResponseBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @RequestMapping("/fellow_count")
    @ResponseBody
    public ResponseBean<Integer> fellowCount(@RequestBody FellowAuthInfo dto){
        try {
            dto.setZts(new String[]{"2", "4"});
            int count = baseService.count(dto);
            return ResponseBean.ok(count);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @RequestMapping("/dept_fellow_count")
    @ResponseBody
    public ResponseBean<Long> deptFellowCount(@RequestBody @Valid IdsDto dto){
        try {
            long count = baseService.countFellows(dto.getIds());
            return ResponseBean.ok(count);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    public  List<String[]> getExportHead(String bizId){
        List<String[]> header = new ArrayList<String[]>();
        List<BizField> columns = bFieldService.listTableFields(bizId);
        if( null != columns && columns.size() > 0){
            for(BizField field:columns) {
                header.add(new String[]{field.getFieldid().toLowerCase(), field.getFielddesc()});
            }
        }
        return header;
    }

    @RequestMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, FellowAuthInfoQueryDto dto)throws Exception  {
        List<FellowAuthInfo> dataList = baseService.list(dto);
        List<String[]> header = getExportHead(bizId)   ;
        try {
            ExcelStUtil.exportForData(response, request, "校友认证信息", header, dataList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("导出失败，有可能是数据过多， 请输入查询条件之后再导出！");
        }

    }
}
