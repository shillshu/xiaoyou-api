package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.MessageDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface MessageDetailService extends IService<MessageDetail> {
    public List<MessageDetail> findByParentId(String parentId);

    public MessageDetail findByRelationId(String relationId);

    List findMessageByfellowId(String receiveId, String sendId);

    void AddMessageAndRelation(String receiveId, String sendId, MessageDetail messageDetail);

    void AddMessage(String messageDetailId, String sendId, MessageDetail messageDetail);

    List findMyMessageList(String fellowId,int type);

    public List findMyMsgList(String fellowId);

    public int findMyMsgCount(String fellowId);

}
