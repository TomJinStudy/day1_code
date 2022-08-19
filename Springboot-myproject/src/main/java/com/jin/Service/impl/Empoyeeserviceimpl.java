package com.jin.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Service.EmpoyeeService;
import com.jin.dao.Employeedao;
import com.jin.domain.Employee;
import org.springframework.stereotype.Service;

@Service
public class Empoyeeserviceimpl extends ServiceImpl<Employeedao, Employee> implements EmpoyeeService {
}
