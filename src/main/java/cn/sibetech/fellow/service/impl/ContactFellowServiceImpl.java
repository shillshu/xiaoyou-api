package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.util.Identities;
import cn.sibetech.core.util.ParamUtil;
import cn.sibetech.fellow.domain.ContactFellow;
import cn.sibetech.fellow.mapper.ContactFellowMapper;
import cn.sibetech.fellow.service.ContactFellowService;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ContactFellowServiceImpl extends ServiceImpl<ContactFellowMapper, ContactFellow> implements ContactFellowService {
    @Override
    public IPage<ContactFellow> page(ContactFellow dto, int current, int size) {
        Page<ContactFellow> page = new Page<ContactFellow>(current, size);
        List<OrderItem> list = new ArrayList<OrderItem>();
        OrderItem item = new OrderItem();
        item.setColumn("when_created");
        item.setAsc(false);
        list.add(item);
        page.setOrders(list);
        QueryWrapper<ContactFellow> wrapper = new QueryWrapper<>();
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public void deleteByContactId(String contactId){
        QueryWrapper<ContactFellow> wrapper= new QueryWrapper<>();
        wrapper.eq("contact_id", contactId);
        baseMapper.delete(wrapper);
    }

    @Override
    public void addContactFellow(List<ContactFellow> fellowList, String contactId){
        if(CollectionUtils.isNotEmpty(fellowList)){
            for(ContactFellow fellow : fellowList){
                fellow.setId(Identities.uuid());
                fellow.setContactId(contactId);
                baseMapper.insert(fellow);
            }
        }
    }

    @Override
    public List findByContactId(String contactId){
        QueryWrapper<ContactFellow> wrapper= new QueryWrapper<>();
        wrapper.eq("contact_id", contactId);
        return baseMapper.selectList(wrapper);
    }

}

