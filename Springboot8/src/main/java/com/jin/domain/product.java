package com.jin.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class product {
    private int id;
    private String pic;
    private String 中文品名;
    private Double size;
    private String description;
    private String packag;
    private int price;
    private  int inner1;
    private  int outsource;
    private  String 外销报价单号;
    private  int customerid;
    @TableField(exist = false)
    private int 销售数量;
    @TableField(exist = false)
    private int 成交金额;
    @TableField(exist = false)
    private int count1;
    @TableField(exist = false)
    private String 费用名称;
    @TableField(exist = false)
    private int 费用金额;

   // private  int sumPrice;
}
