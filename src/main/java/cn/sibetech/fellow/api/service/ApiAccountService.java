package cn.sibetech.fellow.api.service;


import java.util.Map;

public interface ApiAccountService {

    /*通过 校友填写的信息 判断是否可以找到唯一   找到 返回fellow_id  否则返回null*/
    public String findFellow(String xm, String rxnf, String bynf, String csrq, String xh, String xl);

    /*通过 校友填写的信息 判断是否可以找到唯一   找到 返回fellow_id  否则返回null*/
    public String findFellow(String xm, String rxnf, String bynf, String csrq, String xl);

    /*根据fellowId 找到该校友的详细信息 用于确认*/
    public Map findFellowInfoByFellowId(String fellowId);

    public Map findByWxId(String wxId);
}
