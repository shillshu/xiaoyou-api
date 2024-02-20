package cn.sibetech.fellow.controller;

import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.core.util.excel.ExcelTemplateParam;
import cn.sibetech.core.util.excel.ExcelUtil;
import cn.sibetech.core.util.excel.RowModel;
import cn.sibetech.fellow.domain.ActivityFellow;
import cn.sibetech.fellow.domain.Donation;
import cn.sibetech.fellow.domain.DonationItem;
import cn.sibetech.fellow.domain.dto.DonationItemQueryDto;
import cn.sibetech.fellow.domain.dto.DonationQueryDto;
import cn.sibetech.fellow.domain.validate.DonationItemValidate;
import cn.sibetech.fellow.domain.validate.DonationValidate;
import cn.sibetech.fellow.service.DonationItemService;
import cn.sibetech.fellow.service.DonationService;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.sibetech.core.util.SecurityConstants.*;


@Api(tags = "捐赠信息")
@Slf4j
@RestController
@RequestMapping("/donation")
public class DonationController {

    @Autowired
    private DonationService baseService;

    @Autowired
    private DonationItemService itemService;


    @ApiOperation(value = "管理")
    @PostMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<Donation>> page(@RequestBody @Valid DonationQueryDto dto) {
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
    public ResponseBean<String> create(@RequestBody @Valid Donation dto) {
        List<String> errors = DonationValidate.validate(dto, true);
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
    public ResponseBean<String> update(@RequestBody @Valid Donation dto) {
        List<String> errors = DonationValidate.validate(dto, false);
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
    public ResponseBean<Donation> detail(@RequestBody @Valid IdDto dto) {
        try {
            Donation item = baseService.getById(dto.getId());
            return ResponseBean.ok(item);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "获取学院捐赠统计信息")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/tj_data_xy")
    public ResponseBean<List<Map>> xyTjData(@RequestBody @Valid IdDto dto) {
        try {
            List<Map> list = baseService.findXyTjList(dto.getId());
            return ResponseBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    /*导出*/
    @RequestMapping("/export_donation")
    public void exportDonation(HttpServletResponse response, @RequestBody IdDto dto) throws Exception {
        DonationItem item = itemService.getById(dto.getId());
        Donation donation = new Donation();
        donation.setItemId(dto.getId());
        donation.setStatus("1");
        List dataList = baseService.findList(donation);
        ExcelTemplateParam param = new ExcelTemplateParam();
        param.setTemplatePath("/static/excel/donation_item.xlsx");
        param.setFilename("捐赠信息_" + item.getName());
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
