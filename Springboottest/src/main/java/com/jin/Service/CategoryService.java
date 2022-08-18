package com.jin.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jin.domain.Category;
import com.jin.domain.Employee;

public interface CategoryService extends IService<Category> {
  public void  remove(Long ids);
}
