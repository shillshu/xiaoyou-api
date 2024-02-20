package cn.sibetech.fellow.controller;

import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.fellow.domain.ActivityItem;
import cn.sibetech.fellow.domain.dto.ActivityItemQueryDto;
import cn.sibetech.fellow.domain.validate.ActivityItemValidate;
import cn.sibetech.fellow.service.ActivityItemService;
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


@Api(tags = "活动类型")
@Slf4j
@RestController
@RequestMapping("/activity_item")
public class ActivityItemController {

    @Autowired
    private ActivityItemService baseService;



    @ApiOperation(value = "管理")
    @PostMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<ActivityItem>> page(@RequestBody @Valid ActivityItemQueryDto dto) {
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
    public ResponseBean<String> create(@RequestBody @Valid ActivityItem dto) {
        List<String> errors = ActivityItemValidate.validate(dto, true);
        if(CollectionUtils.isNotEmpty(errors)){
            return ResponseBean.error(StringUtils.join(errors));
        }
        try {
            baseService.save(dto);
            return ResponseBean.okMsg("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "修改")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/update")
    public ResponseBean<String> update(@RequestBody @Valid ActivityItem dto) {
        List<String> errors = ActivityItemValidate.validate(dto, false);
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
    public ResponseBean<ActivityItem> detail(@RequestBody @Valid IdDto dto) {
        try {
            ActivityItem vo = baseService.getById(dto.getId());
            return ResponseBean.ok(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "选择")
    @PostMapping("/select_list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<List<SelectModel>> itemSelect() {
        ActivityItem item = new ActivityItem();
        item.setValid("有效");
        List<ActivityItem> list = baseService.findList(item);
        List<SelectModel> result = new ArrayList();
        if (list != null && list.size() > 0) {
            for (ActivityItem dto : list) {
                SelectModel data = new SelectModel();
                data.setValue(dto.getId());
                data.setLabel(dto.getItemName());
                result.add(data);
            }
        }
        return ResponseBean.ok(result);
    }


}
