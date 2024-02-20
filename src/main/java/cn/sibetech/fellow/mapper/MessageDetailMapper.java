package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.MessageDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface MessageDetailMapper extends BaseMapper<MessageDetail> {
    List<MessageDetail> findList(@Param("dto") MessageDetail dto);

    List<MessageDetail> findByParentId(@Param("parentId") String  parentId);

    List<MessageDetail> findSendList(@Param("fellowId") String  fellowId);

    List<MessageDetail> findReceiveList(@Param("fellowId") String  fellowId);

    List<MessageDetail> findMyMsgList(@Param("fellowId") String  fellowId);
}
