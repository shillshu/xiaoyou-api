package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "xy_order")
public class Order extends BaseModel {
    @TableField
    private String body;
    @TableField
    private String itemId; //'捐赠项目id(商品id)'
    @TableField
    private String orderNo; //'订单号'
    @TableField
    private String je; //'订单金额'
    @TableField
    private String status; //'订单状态：0等待支付，1支付成功、2失败、3未知'
    @TableField
    private String bankCode; //'支付渠道编码'
    @TableField
    private String bankTime; //'银行处理时间'
    @TableField
    private String phone; //'手机号'
    @TableField
    private String clbj; //'处理标记：0未处理，1已处理'
    @TableField
    private String clsj; // '处理时间'
    @TableField
    private String client; //'终端名称'
    @TableField
    private String remark; //'备注'
}
