package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.util.Identities;
import cn.sibetech.fellow.domain.MessageDetail;
import cn.sibetech.fellow.domain.MessageRelation;
import cn.sibetech.fellow.mapper.MessageDetailMapper;
import cn.sibetech.fellow.service.MessageDetailService;
import cn.sibetech.fellow.service.MessageRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessageDetailServiceImpl extends ServiceImpl<MessageDetailMapper, MessageDetail> implements MessageDetailService {
    @Autowired
    private MessageRelationService relationService;

    @Override
    public List<MessageDetail> findByParentId(String parentId){
        return baseMapper.findByParentId(parentId);
    }

    @Override
    public MessageDetail findByRelationId(String relationId){
        MessageDetail detail = new MessageDetail();
        detail.setRelationId(relationId);
        List<MessageDetail> list = baseMapper.findList(detail);
        if( null != list && list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List findMessageByfellowId(String receiveId, String sendId){
        MessageDetail detail = new MessageDetail();
        detail.setReceiveId(receiveId);
        detail.setSendId(sendId);
        List<MessageDetail> list = baseMapper.findList(detail);

        if( null != list  && list.size() > 0){
            for( MessageDetail d : list){
                d.setSonList(baseMapper.findByParentId(d.getId()));
            }
        }
        return list;
    }

    @Override
    public void AddMessageAndRelation(String receiveId, String sendId, MessageDetail messageDetail){
        MessageRelation messageRelation = new MessageRelation();
        messageRelation.setId(Identities.uuid());
        messageRelation.setReceiveId(receiveId);
        messageRelation.setSendId(sendId);
        relationService.save(messageRelation);

        messageDetail.setId(Identities.uuid());
        messageDetail.setFellowId(sendId);
        messageDetail.setRelationId(messageRelation.getId());
        baseMapper.insert(messageDetail);
    }

    @Override
    public void AddMessage(String messageDetailId, String sendId, MessageDetail messageDetail){
        MessageDetail messageParent = baseMapper.selectById(messageDetailId);
        messageDetail.setId(Identities.uuid());
        messageDetail.setFellowId(sendId);
        messageDetail.setParentId(messageDetailId);
        messageDetail.setRelationId(messageParent.getRelationId());
        baseMapper.insert(messageDetail);
    }

    @Override
    public List findMyMessageList(String fellowId,int type){
        if(type==0){
            return baseMapper.findSendList(fellowId);
        }else if(type==1) {
            return baseMapper.findReceiveList(fellowId);
        }else{
            return null;
        }
    }

    @Override
    public List findMyMsgList(String fellowId){
        return baseMapper.findMyMsgList(fellowId);
    }

    @Override
    public int findMyMsgCount(String fellowId){
        if(StringUtils.isNotEmpty(fellowId)) {
            List list = baseMapper.findMyMsgList(fellowId);
            if (null == list) {
                return 0;
            } else {
                return list.size();
            }
        }else{
            return 0;
        }
    }

}

