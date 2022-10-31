package com.jin.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Mapper.productMapper;
import com.jin.Service.productService;
import com.jin.domain.product;
import org.springframework.stereotype.Service;

@Service
public class productserviceimpl extends ServiceImpl<productMapper, product> implements productService {
}
