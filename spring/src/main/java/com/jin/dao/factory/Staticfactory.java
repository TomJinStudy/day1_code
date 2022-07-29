package com.jin.dao.factory;

import com.jin.dao.impl.userdaoimpl;
import com.jin.dao.usedao;

public class Staticfactory {
    public static usedao getuserdao(){
       return new userdaoimpl();
    }
}
