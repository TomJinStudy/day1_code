package com.jin.Controller.util;

import jdk.internal.dynalink.beans.StaticClass;

public class Basecontext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    private  static  Long id;

    public static void  setThreadLocal(Long id){
        threadLocal.set(id);
                                               }

    public static Long getId() {
        return id;
    }

    public static void setId(Long id) {
        Basecontext.id = id;
    }

    public static Long getThreadLocal(){
        return threadLocal.get();
    }

}
