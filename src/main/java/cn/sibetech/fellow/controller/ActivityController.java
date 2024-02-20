package cn.sibetech.fellow.controller;

import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.domain.dto.IdsDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.core.util.excel.ExcelTemplateParam;
import cn.sibetech.core.util.excel.ExcelUtil;
import cn.sibetech.core.util.excel.RowModel;
import cn.sibetech.fellow.domain.Activity;
import cn.sibetech.fellow.domain.ActivityFellow;
import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.dto.ActivityFellowQueryDto;
import cn.sibetech.fellow.domain.dto.ActivityQueryDto;
import cn.sibetech.fellow.domain.validate.ActivityValidate;
import cn.sibetech.fellow.service.ActivityFellowService;
import cn.sibetech.fellow.service.ActivityService;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static cn.sibetech.core.util.SecurityConstants.*;


@Api(tags = "活动")
@Slf4j
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService baseService;

    @Autowired
    private ActivityFellowService aFellowService;


    @ApiOperation(value = "管理")
    @PostMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<Activity>> page(@RequestBody @Valid ActivityQueryDto dto) {
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
    public ResponseBean<String> create(@RequestBody @Valid Activity dto) {
        List<String> errors = ActivityValidate.validate(dto, true);
        if(CollectionUtils.isNotEmpty(errors)){
            return ResponseBean.error(StringUtils.join(errors));
        }
        try {
            baseService.addActivity(dto);
            return ResponseBean.okMsg("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "修改")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/update")
    public ResponseBean<String> update(@RequestBody @Valid Activity dto) {
        List<String> errors = ActivityValidate.validate(dto, false);
        if(CollectionUtils.isNotEmpty(errors)){
            return ResponseBean.error(StringUtils.join(errors));
        }
        try {
            baseService.updateById(dto);
            return ResponseBean.okMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/delete")
    public ResponseBean<String> delete(@RequestBody @Valid IdsDto dto) {
        try {
            if (null == dto || null == dto.getIds() || dto.getIds().size() == 0) {
                return ResponseBean.error("参数错误,id不能为空");
            }
            baseService.removeBatchByIds(dto.getIds());
            return ResponseBean.okMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "获取基本信息")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/detail")
    public ResponseBean<Activity> detail(@RequestBody @Valid IdDto dto) {
        try {
            Activity vo = baseService.findById(dto.getId());
            return ResponseBean.ok(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "活动参与人员")
    @PostMapping("/fellowInfoPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<FellowAuthInfo>> fellowInfoPage(@RequestBody @Valid ActivityQueryDto dto) {
        try {
            IPage<FellowAuthInfo> page = aFellowService.page(dto.getActivityIds(), dto.getPageNum(), dto.getPageNum());
            return ResponseBean.ok(page);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }


    @ApiOperation(value = "活动参与人员")
    @PostMapping("/fellowPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<ActivityFellow>> fellowPage(@RequestBody @Valid ActivityFellowQueryDto dto) {
        try {
            return ResponseBean.ok(aFellowService.queryPage(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }



    /*导出*/
    @RequestMapping("/exportFellow")
    public void exportFellow(HttpServletResponse response,@RequestBody ActivityFellow dto) throws Exception {
        List dataList =aFellowService.list(dto);
        ExcelTemplateParam param = new ExcelTemplateParam();
        param.setTemplatePath("/static/excel/activity_fellow.xlsx");
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

    @ApiOperation(value = "选择")
    @PostMapping("/select_list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<List<SelectModel>> itemSelect() {
        List<Activity> list = baseService.findList(new Activity());
        List<SelectModel> result = new ArrayList();
        if (list != null && list.size() > 0) {
            for (Activity dto : list) {
                SelectModel data = new SelectModel();
                data.setValue(dto.getId());
                data.setLabel(dto.getName());
                result.add(data);
            }
        }
        return ResponseBean.ok(result);
    }

    @ApiOperation(value = "count")
    @PostMapping("/fellow_count")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<Long> fellowCount(@RequestBody @Valid IdsDto dto){
        if(CollectionUtils.isNotEmpty(dto.getIds())){
            return ResponseBean.ok(aFellowService.count(dto.getIds()));
        }else {
            return ResponseBean.ok(0l);
        }
    }

}
