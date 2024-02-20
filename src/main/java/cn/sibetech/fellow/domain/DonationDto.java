package cn.sibetech.fellow.domain;

import lombok.Data;

@Data
public class DonationDto {
    //'捐赠人性质'
    private String donationItemId;
    private String jzrxz;
    private String jzje;
    private String bz;
    private String xm;
    private String jzrxm;
    private String xy;
    private String tel;
    private String email;
    private String jzxyId;
    private String jzxy;
    private String zy;
    private String zyId;
    private String rxnf;

    private String sfxyfp;

    private String fptt;

    private Integer current;

    private Integer size;
}
