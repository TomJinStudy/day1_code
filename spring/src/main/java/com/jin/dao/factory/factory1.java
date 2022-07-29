package com.jin.dao.factory;

import com.jin.dao.impl.userdaoimpl;
import com.jin.dao.usedao;

public class factory1 {

        public  usedao getuserdao(){
            return new userdaoimpl();
        }
}
