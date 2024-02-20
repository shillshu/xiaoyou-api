package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.util.Identities;
import cn.sibetech.fellow.domain.Contact;
import cn.sibetech.fellow.domain.dto.ContractQueryDto;
import cn.sibetech.fellow.mapper.ContactMapper;
import cn.sibetech.fellow.service.ContactFellowService;
import cn.sibetech.fellow.service.ContactService;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact> implements ContactService {
    @Autowired
    private ContactFellowService cFellowService;
    @Override
    public IPage<Contact> queryPage(ContractQueryDto dto) {
        Page<Contact> page = new Page<Contact>(dto.getPageNum(), dto.getPageSize());
        List<OrderItem> list = new ArrayList<OrderItem>();
        OrderItem item = new OrderItem();
        item.setColumn("when_created");
        item.setAsc(false);
        list.add(item);
        page.setOrders(list);
        QueryWrapper<Contact> wrapper = new QueryWrapper<>();
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public void add(Contact contact){
        contact.setId(Identities.uuid());
        cFellowService.addContactFellow(contact.getFellowList(), contact.getId());
        baseMapper.insert(contact);
    }

    @Override
    public void edit(Contact contact){
        cFellowService.deleteByContactId(contact.getId());
        cFellowService.addContactFellow(contact.getFellowList(), contact.getId());
        baseMapper.updateById(contact);
    }

    @Override
    public void remove(String contactId){
        cFellowService.deleteByContactId(contactId);
        baseMapper.deleteById(contactId);
    }


}

