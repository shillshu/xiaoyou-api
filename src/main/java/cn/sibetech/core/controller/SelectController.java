package cn.sibetech.core.controller;

import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.core.service.SelectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

import static cn.sibetech.core.util.SecurityConstants.*;

@Api(tags = "选择")
@Slf4j
@RestController
@RequestMapping("/select")
public class SelectController {

    @Resource
    private SelectService baseService;

    @ApiOperation(value = "学年")
    @PostMapping("/xn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<List<SelectModel>> xn() {
        try {
            List<SelectModel> xnList = new ArrayList<SelectModel>();
            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR);
            for (int i = 0; i < 10; i++) {
                String value = (currentYear - i) + "-" + (currentYear + 1 - i);
                SelectModel dto = new SelectModel();
                dto.setValue(value);
                dto.setLabel(value);
                xnList.add(dto);
            }
            return ResponseBean.ok(xnList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "年级")
    @PostMapping("/nj")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<List<SelectModel>> nj() {
        try {
            List<SelectModel> njList = new ArrayList<SelectModel>();
            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR) ;
            for (int i = 0; i < 10; i++) {
                int value = currentYear - i;
                SelectModel dto = new SelectModel();
                dto.setValue(String.valueOf(value));
                dto.setLabel(String.valueOf(value));
                njList.add(dto);
            }
            return ResponseBean.ok(njList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @GetMapping("/select_dept/{fatherId}")
    public ResponseBean<List<SelectModel>> selectDept(@PathVariable String fatherId){
        return ResponseBean.ok(baseService.selectDeptList(fatherId));
    }


    @RequestMapping("/xy")
    public ResponseBean<List<SelectModel>> xylist() {
        return ResponseBean.ok(baseService.selectXueyuanList());
    }

    @RequestMapping("/zy")
    public ResponseBean<List<SelectModel>> zylist(String xyId) {
        return ResponseBean.ok(baseService.selectMajorList(xyId));
    }

    @RequestMapping("/nj")
    public ResponseBean<List<SelectModel>> njlist() {
        return ResponseBean.ok(baseService.selectNjList());
    }

    @RequestMapping("/bj")
    public ResponseBean<List<SelectModel>> bjlist(String xyId, String zyId, String nj) {
        return ResponseBean.ok(baseService.selectClassList(xyId, zyId, nj));
    }

    @GetMapping("/select_code_items/{codeKindId}")
    public ResponseBean<List<SelectModel>> selectCodeItems(@PathVariable String codeKindId){
        return ResponseBean.ok(baseService.selectCodeItemList(codeKindId));
    }


}
