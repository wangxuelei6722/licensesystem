package com.wangxl.licensesystem.service;

import com.alibaba.fastjson.JSONObject;
import com.wangxl.licensesystem.pojo.Role;
import com.wangxl.licensesystem.pojo.Users;
import com.wangxl.licensesystem.utils.PageInfoUtil;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UsersService
 * @Description : 用户service
 *
 * @Author : Wangxl
 * @Date : 2020/5/8 18:38
*/
public interface UsersService {

    /**
     * @MethodName : saveOneUsersService
     * @Description : 添加一条用户记录
     * @Param :
     * @Return : com.alibaba.fastjson.JSONObject
     * @Author : Wangxl
     * @Date : 2020/5/8 18:40
    */
    JSONObject saveOneUsersService(Users users);

    /**
     * @MethodName : updateOneUsersService
     * @Description : 更新一条管理员
     * @Param :
     * @Return : com.alibaba.fastjson.JSONObject
     * @Author : Wangxl
     * @Date : 2020/5/8 18:44
    */
    JSONObject updateOneUsersService(Users users);

    /**
     * @MethodName : deleteOneUsersService
     * @Description : 删除一条用户
     * @Param :
     * @Return : com.alibaba.fastjson.JSONObject
     * @Author : Wangxl
     * @Date : 2020/5/8 18:44
    */
    JSONObject deleteOneUsersService(Map<String, Object> condMap);

    /**
     * @MethodName : findOneUsersService
     * @Description : 查询一条用户
     * @Param :
     * @Return : com.wangxl.licensesystem.pojo.Users
     * @Author : Wangxl
     * @Date : 2020/5/8 18:45
    */
    Users findOneUsersService(Map<String, Object> condMap);

    /**
     * @MethodName : findCondListUsersService
     * @Description : 查询多条用户记录
     * @Param :
     * @Return : java.util.Map<java.lang.String,java.lang.Object>
     * @Author : Wangxl
     * @Date : 2020/5/8 18:45
    */
    Map<String,Object> findCondListUsersService(PageInfoUtil pageInfoUtil,Map<String,Object> condMap);
    /*---- 管理员操作结束 ----*/

    /*---- 角色操作开始 ----*/
    /**
     * @MethodName : saveOneRoleService
     * @Description : 保存一条角色
     * @Param :
     * @Return : com.alibaba.fastjson.JSONObject
     * @Author : Wangxl
     * @Date : 2020/5/8 18:45
    */
    JSONObject saveOneRoleService(Role role);

    /**
     * @MethodName : updateOneRoleService
     * @Description : 更新一条角色
     * @Param :
     * @Return : com.alibaba.fastjson.JSONObject
     * @Author : Wangxl
     * @Date : 2020/5/8 18:45
    */
    JSONObject updateOneRoleService(Role role);

    /**
     * @MethodName : deleteOneRoleService
     * @Description : 删除一条角色
     * @Param :
     * @Return : com.alibaba.fastjson.JSONObject
     * @Author : Wangxl
     * @Date : 2020/5/8 18:46
    */
    JSONObject deleteOneRoleService(Map<String, Object> condMap);


    /**
     * @MethodName : findOneRoleService
     * @Description : 查询一条角色
     * @Param :
     * @Return : com.wangxl.licensesystem.pojo.Role
     * @Author : Wangxl
     * @Date : 2020/5/8 18:46
    */
    Role findOneRoleService(Map<String, Object> condMap);

    /**
     * @MethodName : findCondListRoleService
     * @Description : 查询多条角色记录
     * @Param :
     * @Return : java.util.List<com.wangxl.licensesystem.pojo.Role>
     * @Author : Wangxl
     * @Date : 2020/5/8 18:46
    */
    List<Role> findCondListRoleService(PageInfoUtil pageInfoUtil, Map<String,Object> condMap);
}
