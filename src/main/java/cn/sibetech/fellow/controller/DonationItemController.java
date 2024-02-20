package cn.sibetech.fellow.controller;

import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.fellow.domain.ActivityItem;
import cn.sibetech.fellow.domain.DonationItem;
import cn.sibetech.fellow.domain.dto.ActivityItemQueryDto;
import cn.sibetech.fellow.domain.dto.DonationItemQueryDto;
import cn.sibetech.fellow.domain.validate.ActivityItemValidate;
import cn.sibetech.fellow.domain.validate.DonationItemValidate;
import cn.sibetech.fellow.service.ActivityItemService;
import cn.sibetech.fellow.service.DonationItemService;
import com.alibaba.fastjson2.JSONArray;
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


@Api(tags = "捐赠类型")
@Slf4j
@RestController
@RequestMapping("/donation_item")
public class DonationItemController {

    @Autowired
    private DonationItemService baseService;



    @ApiOperation(value = "管理")
    @PostMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<DonationItem>> page(@RequestBody @Valid DonationItemQueryDto dto) {
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
    public ResponseBean<String> create(@RequestBody @Valid DonationItem dto) {
        List<String> errors = DonationItemValidate.validate(dto, true);
        if(CollectionUtils.isNotEmpty(errors)){
            return ResponseBean.error(StringUtils.join(errors));
        }
        try {
            if(CollectionUtils.isNotEmpty(dto.getJeList())){
                dto.setJe(JSONArray.toJSONString(dto.getJeList()));
            }
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
    public ResponseBean<String> update(@RequestBody @Valid DonationItem dto) {
        List<String> errors = DonationItemValidate.validate(dto, false);
        if(CollectionUtils.isNotEmpty(errors)){
            return ResponseBean.error(StringUtils.join(errors));
        }
        try {
            if(CollectionUtils.isNotEmpty(dto.getJeList())){
                dto.setJe(JSONArray.toJSONString(dto.getJeList()));
            }
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
    public ResponseBean<DonationItem> detail(@RequestBody @Valid IdDto dto) {
        try {
            DonationItem item = baseService.getById(dto.getId());
            if(StringUtils.isNotEmpty(item.getJe())){
                item.setJeList((JSONArray.parseArray(item.getJe())));
            }else{
                item.setJeList(new ArrayList());
            }
            return ResponseBean.ok(item);
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
        DonationItemQueryDto item = new DonationItemQueryDto();
        item.setValid("有效");
        List<DonationItem> list = baseService.findList(item);
        List<SelectModel> result = new ArrayList();
        if (list != null && list.size() > 0) {
            for (DonationItem dto : list) {
                SelectModel data = new SelectModel();
                data.setValue(dto.getId());
                data.setLabel(dto.getName());
                result.add(data);
            }
        }
        return ResponseBean.ok(result);
    }


}
