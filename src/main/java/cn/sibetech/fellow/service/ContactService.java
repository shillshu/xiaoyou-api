package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.Contact;
import cn.sibetech.fellow.domain.dto.ContractQueryDto;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



public interface ContactService extends IService<Contact> {
    public IPage<Contact> queryPage(ContractQueryDto dto);

    public void add(Contact contact);

    public void edit(Contact contact);

    public void remove(String contactId);
}
