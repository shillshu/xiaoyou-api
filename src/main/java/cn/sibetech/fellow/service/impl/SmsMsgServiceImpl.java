package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.domain.ShiroUser;
import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.util.HttpConnectionUtil;
import cn.sibetech.core.util.Identities;
import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.SmsMsg;
import cn.sibetech.fellow.domain.SmsMsgNotice;
import cn.sibetech.fellow.domain.SmsUser;
import cn.sibetech.fellow.mapper.SmsMsgMapper;
import cn.sibetech.fellow.service.SmsMsgNoticeService;
import cn.sibetech.fellow.service.SmsMsgService;
import cn.sibetech.fellow.service.SmsUserService;
import cn.sibetech.fellow.util.XyConstant;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.qcloudsms.SmsSenderUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class SmsMsgServiceImpl extends ServiceImpl<SmsMsgMapper, SmsMsg> implements SmsMsgService {
    public static final int DX_HD_TEL_ID = 197612;//197612
    public static final int DX_ZF_TEL_ID = 198589;
    public static final int DX_YDZF_TEL_ID = 257287;
    public static final int DX_CJZF_TEL_ID = 271932;

    public static final String SMS_TYPE_CODE_HD = "0";
    public static final String SMS_TYPE_CODE_ZF = "1";
    public static final String SMS_TYPE_CODE_YDZF = "2";
    public static final String SMS_TYPE_CODE_CJZF = "3";
    @Autowired
    private SmsUserService msgUserService;

    @Autowired
    private SmsMsgNoticeService msgNoticeService;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public IPage<SmsMsg> page(SmsMsg dto, int current, int size) {
        Page<SmsMsg> page = new Page<>(current, size);
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public int getThreadProgress(String id) {
        String progressStr = String.valueOf(ObjectUtils.defaultIfNull(redisTemplate.opsForValue().get("progress-"+id), ""));
        if(StringUtils.isNotEmpty(progressStr)){
            return Integer.parseInt(progressStr);
        }
        return 0;
    }

    @Override
    public String add(String content,String typeCode, List<FellowAuthInfo> users) {
        SmsMsg msg = new SmsMsg();
        msg.setId(Identities.uuid());
        msg.setContent(content);
        msg.setSenderId(SecurityContextHolder.getCurrentUser().getUserId());
        msg.setTypeCode(typeCode);
        SendSmsThread thread = new SendSmsThread(msg, users);
        thread.start();
        return msg.getId();
    }

    private class SendSmsThread extends Thread {
        private SmsMsg msg;
        private List<FellowAuthInfo> userList;

        public SendSmsThread(SmsMsg msg, List<FellowAuthInfo> userList) {
            this.msg = msg;
            this.userList = userList;
        }

        public void run() {
            if (userList != null && userList.size() > 0) {
                baseMapper.insert(msg);
                List<SmsUser> wxUsers = new ArrayList<SmsUser>();
                int count = 0;
                String mobile = "";
                List telList = new ArrayList();
                String[] params = new String[1];
                String typeCode = msg.getTypeCode();
                params[0] = msg.getContent();
                if (StringUtils.isNotEmpty(typeCode)) {
                    for (int i = 0; i < userList.size(); i++) {
                        Map map = new HashedMap();
                        FellowAuthInfo r = userList.get(i);
                        String tel = r.getSj();
                        String code = r.getCode();
                        if (StringUtils.isNotEmpty(tel) && StringUtils.isNotEmpty(code)) {
                            SmsUser msgUser = new SmsUser();
                            msgUser.setId(Identities.uuid());
                            msgUser.setMsgId(msg.getId());
                            msgUser.setStatus("1");
                            msgUser.setUserId(r.getId());
                            wxUsers.add(msgUser);
                            if (StringUtils.isEmpty(mobile)) {
                                mobile = tel;
                            } else {
                                mobile += "," + tel;
                            }
                            map.put("mobile", tel);
                            map.put("nationcode", code);
                            telList.add(map);
                            count++;
                        }
                        if (count == 100 || (i + 1) == userList.size()) {
                            msgUserService.saveBatch(wxUsers);
                            if (typeCode.equals(SMS_TYPE_CODE_HD)) {
                                send(count, "", "", params, mobile, telList, DX_HD_TEL_ID);
                            } else if (typeCode.equals(SMS_TYPE_CODE_ZF)) {
                                send(count, "", "", params, mobile, telList, DX_ZF_TEL_ID);
                            } else if (typeCode.equals(SMS_TYPE_CODE_YDZF)) {
                                send(count, "", "", params, mobile, telList, DX_YDZF_TEL_ID);
                            } else if (typeCode.equals(SMS_TYPE_CODE_CJZF)) {
                                send(count, "", "", params, mobile, telList, DX_CJZF_TEL_ID);
                            }
                            mobile = "";
                            telList.clear();
                            count = 0;
                            wxUsers.clear();
                        }
                    }
                }
            }
        }
        private void send( int userCount, String extend, String ext,String[] params,String mobile,List telList,int tplId ){
            try {
                String url = "https://yun.tim.qq.com/v5/tlssmssvr/sendmultisms2";
                long random = SmsSenderUtil.getRandom();
                long now = SmsSenderUtil.getCurrentTime();
                org.json.JSONObject body = (new org.json.JSONObject()).put("tel",telList).put("tpl_id",tplId).put("params", params).put("type","0").put("sig", SmsSenderUtil.calculateSignature( XyConstant.DX_SECRET, random, now, mobile)).put("time", now).put("extend", SmsSenderUtil.isNotEmpty(extend)?extend:"").put("ext", SmsSenderUtil.isNotEmpty(ext)?ext:"");
                String result = HttpConnectionUtil.post(url + "?sdkappid=" + XyConstant.DX_APPID+"&random="+random, body.toString());
                SmsMsgNotice msgNoticeLog = new SmsMsgNotice();
                msgNoticeLog.setId(Identities.uuid());
                if(params!=null && params.length>0){
                    msgNoticeLog.setSendParam(params[0]);
                }
                msgNoticeLog.setSendResult(result);
                msgNoticeLog.setUserId(msg.getSenderId());
                msgNoticeService.save(msgNoticeLog);
                /*String progressStr = String.valueOf(ObjectUtils.defaultIfNull(redisTemplate.opsForValue().get("progress-"+msg.getId()), ""));
                int progress = 0;
                if(StringUtils.isNotEmpty(progressStr)){
                    progress = Integer.parseInt(progressStr) + userCount;
                }
                else {
                    progress = userCount;
                }
                redisTemplate.opsForValue().set("progress-"+msg.getId(), progress);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

