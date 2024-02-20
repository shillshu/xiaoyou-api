package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.ContactFellow;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ContactFellowService extends IService<ContactFellow> {
    public IPage<ContactFellow> page(ContactFellow dto, int current, int size);

    public void deleteByContactId(String contactId);

    public void addContactFellow(List<ContactFellow> fellowList, String contactId);

    public List findByContactId(String contactId);
}
