package com.wangxl.licensesystem.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class LoginLog {

  private int id;
  private String loginName;
  private String userName;
  private Date loginTime;
  private String loginIpInfo;
  private int userId;




}
