package com.jin.domain;
import lombok.Data;

import java.util.Date;

@Data
public class customer {
    private int id;
    private  String company;
    private  String address;
    private  String Paymentterm;
    private  String tel;
    private  String fax;
    private  int starttime;
    private  String contacts;
    private  String mail;
    private  int endtime;
    private  String to1;
    private  String contact;
    private  String quotation;
    private Date time;
  //  private List<product> products;
}