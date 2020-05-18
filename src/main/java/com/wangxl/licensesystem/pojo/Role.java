package com.wangxl.licensesystem.pojo;


import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class Role {

  private int id;
  private int createId;
  private int updateId;
  private String name;
  private String content;
  private String code;
  private byte status;
  private byte level;
  private String remark;
  private Date createTime;
  private Date updateTime;
  private Date pubTime;

  /* 关联对象 */
  private List<Users> usersList ;


}
