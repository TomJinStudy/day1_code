package com.jin.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Service.AddressBookService;
import com.jin.Service.UserService;
import com.jin.dao.AddressBookDao;
import com.jin.dao.UserDao;
import com.jin.domain.AddressBook;
import com.jin.domain.User;
import org.springframework.stereotype.Service;

@Service
public class AddressBookserviceimpl extends ServiceImpl<AddressBookDao, AddressBook> implements AddressBookService {


}
