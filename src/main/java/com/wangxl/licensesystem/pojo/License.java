package com.wangxl.licensesystem.pojo;


import lombok.Data;

import java.util.Date;
/**
 * @ClassName License
 * @Description : license POJO
 *
 * @Author : Wangxl
 * @Date : 2020/5/7 17:06
*/
@Data
public class License {

  private int id;
  private String insName;
  private String macAddr;
  private Date startDate;
  private Date endDate;
  private String licenseCode;
  private Date createTime;
  private Date updateTime;
  private Date pubTime;




}
