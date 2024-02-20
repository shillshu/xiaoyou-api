package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@TableName(value = "xy_fellow")
public class Fellow extends BaseModel {
    @TableField
    private String dataId;

    @TableField
    private String xm;

    @TableField
    private String xym;

    @TableField
    private String ywm;

    @TableField
    private String xb;

    @TableField
    private String csrq;

    @TableField
    private String xykbh;

    @TableField
    private String szdgb;

    @TableField
    private String szdsf;

    @TableField
    private String szdsx;

    @TableField
    private String jggb;

    @TableField
    private String jgsf;

    @TableField
    private String jgsx;

    @TableField
    private String mz;

    @TableField
    private String zzmm;

    @TableField
    private String bgdh;

    @TableField
    private String jtdh;

    @TableField
    private String email;

    @TableField
    private String cz;

    @TableField
    private String qtlxfs;

    @TableField
    private String fxlxybj;

    @TableField
    private String bz;

    @TableField
    private String tjr;

    @TableField
    private String gxr;

    @TableField
    private String gxrq;

    @TableField
    private String mqzt;

    @TableField
    private String xxdz;

    @TableField
    private String yzbh;

    @TableField
    private String jsxxdz;

    @TableField
    private String grjj;

    @TableField
    private String yddh2;

    @TableField
    private String yxgx;

    @TableField
    private String sfzh;

    @TableField
    private String email2;

    @TableField
    private String sjbz;

    @TableField
    private String sjbz2;

    @TableField
    private String qq;

    @TableField
    private String msn;

    @TableField
    private String nationality;

    @TableField
    private String bh;

    @TableField
    private String importId;

    @TableField
    private String status;

    @TableField( exist = false)
    private String xyId;

    @TableField( exist = false)
    private String xy;

    @TableField( exist = false)
    private String xl;

    @TableField( exist = false)
    private String jyQssj;

    @TableField( exist = false)
    private String jyZzsj;

    @TableField( exist = false)
    private String jyXl;

    @TableField( exist = false)
    private String jyXh;

    @TableField( exist = false)
    private String jyXy;

    @TableField( exist = false)
    private String jyZy;

    @TableField( exist = false)
    private String jyBj;

    @TableField( exist = false)
    private String jyDs;

    @TableField( exist = false)
    private String gzQssj;

    @TableField( exist = false)
    private String gzZzsj;

    @TableField( exist = false)
    private String gzGzdw;

    @TableField( exist = false)
    private String gzGzzw;
    @TableField( exist = false)
    private String gzGzhy;

    @TableField( exist = false)
    private String gzGznr;

    @TableField( exist = false)
    private String gzJzcs;

    @TableField( exist = false)
    private String gzZc;

    @TableField( exist = false)
    private List<FellowResume> resumeList = new ArrayList<>();

    @TableField( exist = false)
    private List<FellowEducation> educationList = new ArrayList<>();

}
