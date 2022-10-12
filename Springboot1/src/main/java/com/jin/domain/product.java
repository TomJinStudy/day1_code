package com.jin.domain;

import lombok.Data;

@Data
public class product {
    private int id;
    private String picture;
    private String moq;
    private Double size;
    private String description;
    private String packag;
    private int price;
    private  int customerid;
}
