package com.wangxl.licensesystem.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName Users
 * @Description : 用户表POJO
 *
 * @Author : Wangxl
 * @Date : 2020/5/7 17:16
*/
@Data
public class Users {

  private int id;
  private int roleId;
  private String nickname;
  private String password;
  private String username;
  private String salt;
  private String email;
  private String phone;
  private int loginCount;
  private int failedCount;
  private Date failedTime;
  private String address;
  private String photoPath;
  private String photoName;
  private double photoSize;
  private byte emailStatus;
  private Date sendEmailTime;
  private String sendEmailCode;
  private String emailUuid;
  private byte phoneStatus;
  private byte sex;
  private byte status;
  private Date createTime;
  private Date updateTime;
  private Date lastLoginTime;

  /* 关联对象 */
  private Role role;


}
