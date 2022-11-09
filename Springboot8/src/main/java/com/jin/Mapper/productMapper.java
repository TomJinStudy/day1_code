package com.jin.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jin.domain.product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface productMapper extends BaseMapper<product> {
    @Select("SELECT 外销报价单号,COUNT(count1) count1 FROM (SELECT  中文品名,COUNT(中文品名) count1,外销报价单号 FROM product where customerid=#{id} GROUP BY 中文品名) a  GROUP BY 外销报价单号 ")
     List<product> resort(int id);

}
