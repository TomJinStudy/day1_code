package com.jin.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户
 *
 * @author shimh
 * <p>
 * 2018年1月23日
 */
@Data
@TableName("sys_user")
public class User {

    private static final long serialVersionUID = -4454737765850239378L;

    private Long id;




    private String account;

    /**
     * 使用md5(username + original password + salt)加密存储
     */

   // @Column(name = "password")
    private String password;

    /**
     * 头像
     */
    private String avatar;

    //@Column(name = "email")
    private String email;  // 邮箱


   // @Column(name = "nickname")
    private String nickname;

  //  @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;


    /**
     * 加密密码时使用的种子
     */
    private String salt;


    /**
     * 创建时间
     */
  //  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    //@Column(name = "create_date")
    private Date createDate;


    /**
     * 最后一次登录时间
     */
   // @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    //@Column(name = "last_login")
    private Date lastLogin;

    /**
     * 系统用户的状态
     */
   // @Enumerated(EnumType.STRING)
  //  private UserStatus status = UserStatus.normal;

    /**
     * 是否是管理员
     */
    private Boolean admin = false;

    /**
     * 逻辑删除flag
     */
    private Boolean deleted = Boolean.FALSE;




}
