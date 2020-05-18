package com.wangxl.licensesystem.pojo;

import lombok.Data;
import org.apache.ibatis.javassist.bytecode.BadBytecode;

import java.util.Date;

/**
 * @ClassName Menus
 * @Description : 菜单POJO
 *
 * @Author : Wangxl
 * @Date : 2020/5/7 17:08
*/
@Data
public class Menus {

  private int id;
  private int parentId;
  private String name;
  private String url;
  private String cssClass;
  private String skey;
  private String parentKey;
  private byte hide;
  private String content;
  private byte isLeafStatus;
  private byte status;
  private int sort;
  private byte level;
  private Date createTime;
  private Date updateTime;
  private Date pubTime;


}
