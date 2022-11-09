package com.jin.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reslut {
  private String Msg;
  private Object data;
  private Boolean success;
  public static  Reslut success(Object data){
    return new Reslut(null,data,true);
  }
  public static Reslut error(String Msg){

    return new Reslut(Msg,null,false);
  }
  public static  Reslut success(){
    return new Reslut(null,null,true);
  }

}
