package com.wangxl.licensesystem.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName RoleMenu
 * @Description : 角色菜单关系POJO
 *
 * @Author : Wangxl
 * @Date : 2020/5/7 17:14
*/
@Data
public class RoleMenu {

  private int id;
  private int roleId;
  private int menuId;
  private int roleOrganizationId;
  private byte status;
  private Date createTime;
  private Date updateTime;
  private Date pubTime;


}
