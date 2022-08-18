package com.jin.Controller.util;

public class Basecontext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();


    public static void  setThreadLocal(Long id){
        threadLocal.set(id);
    }

    public static Long getThreadLocal(){
       return threadLocal.get();
    }
}
