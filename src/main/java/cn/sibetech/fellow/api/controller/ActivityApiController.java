package cn.sibetech.fellow.api.controller;

import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.domain.dto.IdsDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.core.util.DateUtils;
import cn.sibetech.core.util.Identities;
import cn.sibetech.core.util.excel.ExcelTemplateParam;
import cn.sibetech.core.util.excel.ExcelUtil;
import cn.sibetech.core.util.excel.RowModel;
import cn.sibetech.fellow.api.service.JWTAuthService;
import cn.sibetech.fellow.domain.*;
import cn.sibetech.fellow.domain.dto.ActivityFellowQueryDto;
import cn.sibetech.fellow.domain.dto.ActivityQueryDto;
import cn.sibetech.fellow.domain.validate.ActivityValidate;
import cn.sibetech.fellow.service.*;
import cn.sibetech.fellow.util.DistanceUtil;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.sibetech.core.util.SecurityConstants.*;


@Api(tags = "活动")
@Slf4j
@RestController
@RequestMapping("//api/activity")
public class ActivityApiController {

    @Autowired
    private ActivityService baseService;

    @Autowired
    private ActivityFellowService aFellowService;

    @Autowired
    private ActivityMarkService aMarkService;

    @Autowired
    private ActivityItemService aItemService;

    @Autowired
    private FellowAuthInfoService authInfoService;


    @ApiOperation(value = "管理  key isZt")
    @PostMapping("/list")
    public ResponseBean<List<Activity>> list(@RequestBody @Valid Activity dto) {
        try {
            return ResponseBean.ok(baseService.findList(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "我参加的活动")
    @PostMapping("/my_list")
    public ResponseBean<List<Activity>> myList(HttpServletRequest request) {
        try {
            String bindId = JWTAuthService.getBindIdByJWT(request);
            FellowAuthInfo authInfo = authInfoService.findByBindId(bindId);
            if (authInfo == null || StringUtils.isEmpty(authInfo.getFellowId())) {
                return ResponseBean.error("绑定失败");
            }
            List list = baseService.findListByFellowId(authInfo.getFellowId());
            return ResponseBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "活动详细信息")
    @PostMapping("/hd_info")
    public ResponseBean<Activity> hdInfo(HttpServletRequest request, @RequestBody @Valid Activity dto) {
        if( StringUtils.isEmpty(dto.getId())){
            return ResponseBean.error("参数错误！");
        }
        try {
            String bindId = JWTAuthService.getBindIdByJWT(request);
            FellowAuthInfo authInfo = authInfoService.findByBindId(bindId);

            Activity activity = baseService.findById(dto.getId());
            if( null ==  activity ){
                return ResponseBean.error("未找到活动信息！");
            }
            if (authInfo == null || StringUtils.isEmpty(authInfo.getFellowId())) {
                activity.setState("0"); /* 未报名 且没有注册*/
                return ResponseBean.ok(activity);
            }else {
                ActivityFellow activityFellow = aFellowService.findByFellowId(dto.getId(), authInfo.getFellowId(), dto.getIsValid());
                if (activityFellow == null) {
                    activity.setState("1"); /* 未报名*/
                }else{
                    activity.setState("2");/* 已报名*/
                    activity.setActivityFellow(activityFellow);
                }
                return ResponseBean.ok(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "报名")
    @PostMapping("/enter")
    public ResponseBean<String> delete(HttpServletRequest request,@RequestBody @Valid ActivityFellow activityFellow) {
        if( StringUtils.isEmpty(activityFellow.getActivityId())){
            return ResponseBean.error("参数错误！");
        }
        try {
            String bindId = JWTAuthService.getBindIdByJWT(request);
            FellowAuthInfo authInfo = authInfoService.findByBindId(bindId);
            if (authInfo == null && StringUtils.isEmpty(authInfo.getFellowId())) {
                return ResponseBean.error("请重新绑定！");
            }
            if (activityFellow != null && StringUtils.isEmpty(activityFellow.getId())) {
                Activity activity = baseService.getById(activityFellow.getActivityId());
                if (activity == null || StringUtils.isEmpty(activity.getBmStartTime()) || StringUtils.isEmpty(activity.getBmEndTime())) {
                    return ResponseBean.error("活动不存在！");
                }
                if (DateUtils.compareDate( activity.getBmStartTime(), activity.getBmEndTime())) {
                    activityFellow.setId(Identities.uuid());
                    activityFellow.setFellowId(authInfo.getFellowId());
                    activityFellow.setIsValid("0");
                    aFellowService.save(activityFellow);
                    return ResponseBean.ok("报名成功!");
                } else  {
                    return ResponseBean.error("不在报名时间范围之内！");
                }
            } else if (activityFellow != null && StringUtils.isNotEmpty(activityFellow.getId())) {
                aFellowService.updateById(activityFellow);
                return ResponseBean.error("修改成功！");
            }
            return ResponseBean.okMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }


    @ApiOperation(value = "取消报名 id 为activityFellow 的id")
    @PostMapping("/remove")
    public ResponseBean<String> delete(HttpServletRequest request,@RequestBody @Valid IdDto dto) {
        try {
            String bindId = JWTAuthService.getBindIdByJWT(request);
            FellowAuthInfo authInfo = authInfoService.findByBindId(bindId);
            if (authInfo == null && StringUtils.isEmpty(authInfo.getFellowId())) {
                return ResponseBean.error("请重新绑定！");
            }
            aFellowService.removeById(dto.getId());
            return ResponseBean.ok("取消成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "主题活动签到信息  返回空 则可以签到")
    @PostMapping("/sign_info")
    public ResponseBean<ActivityMark> signInfo(HttpServletRequest request) {
        try {
            String bindId = JWTAuthService.getBindIdByJWT(request);
            FellowAuthInfo authInfo = authInfoService.findByBindId(bindId);

            ActivityItem activityItem = aItemService.findZtItem();
            if (activityItem == null) {
                return ResponseBean.error("无主题活动！");
            }
            String fellowId = authInfo.getFellowId();
            if (StringUtils.isNotEmpty(bindId)) {
                if (StringUtils.isNotEmpty(activityItem.getId())) {
                    ActivityMark activityMark = aMarkService.findByFellowId( activityItem.getId(), fellowId);
                    if (activityMark != null) {
                        return ResponseBean.ok(activityMark);
                    } else {
                        return ResponseBean.ok(null);
                    }
                } else {
                    return ResponseBean.error("系统错误！");
                }
            } else {
                return ResponseBean.error("请重新绑定！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }


    @ApiOperation(value = "主题活动签到")
    @PostMapping("/sign")
    public ResponseBean<ActivityMark> sign(HttpServletRequest request,@RequestBody @Valid ActivityMark dto) {
        try {
            String bindId = JWTAuthService.getBindIdByJWT(request);
            FellowAuthInfo authInfo = authInfoService.findByBindId(bindId);
            if (StringUtils.isEmpty(bindId)) {
                return ResponseBean.error("请重新绑定！");
            }
            ActivityItem activityItem = aItemService.findZtItem();
            if (activityItem == null) {
                return ResponseBean.error("无主题活动！");
            }
            double scope = DistanceUtil.getDistance(dto.getLatitude(), dto.getLongitude(), activityItem.getLatitude(), activityItem.getLongitude());
            if (activityItem.getScope() != 0 && scope > activityItem.getScope()) {
                return ResponseBean.error("不在签到范围内！");
            }
            if (StringUtils.isNotEmpty(activityItem.getItemCode())) {
                int orderNo = aMarkService.getOrderNo(activityItem.getItemCode());
                dto.setId(Identities.uuid());
                dto.setFellowId(authInfo.getFellowId());
                dto.setItemId(activityItem.getId());
                dto.setQdsj(DateUtils.formatDateTime(new Date()));
                dto.setOrderNo(orderNo);
                aMarkService.save(dto);
                return ResponseBean.ok(dto);
            } else {
                return ResponseBean.error("系统设置错误！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }


}
