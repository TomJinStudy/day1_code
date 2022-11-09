package com.jin.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jin.domain.customer;
import com.jin.domain.product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface customerMapper extends BaseMapper<customer> {
    @Results({@Result(id=true,column="id",property="id"),
            @Result(column="company",property="company"),
            @Result(column="address",property="address"),
            @Result(column="tel",property="tel"),
            @Result(column="contacts",property="contacts"),
            @Result(column="to1",property="to1"),
            @Result(column="fax",property="fax"),
            @Result(column="productid",property="productid"),
            @Result(column="Paymentterm",property="Paymentterm",many=@Many(select="com.jin.Mapper.customerMapper.getproduct",fetchType= FetchType.LAZY))})
    @Select("SELECT * from customer where productid=#{productid}")
    public customer findid(int productid);
    @Results({@Result(id=true,column="id",property="id"),
            @Result(column="picture",property="picture"),
            @Result(column="moq",property="moq"),
            @Result(column="size",property="size"),
            @Result(column="description",property="description"),
            @Result(column="packag",property="packag"),
            @Result(column="price",property="price")})
    @Select("SELECT * from product where id=#{id}")
    public List<product> getproduct(int id);
}
