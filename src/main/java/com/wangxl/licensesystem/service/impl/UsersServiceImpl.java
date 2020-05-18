package com.wangxl.licensesystem.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.wangxl.licensesystem.pojo.Role;
import com.wangxl.licensesystem.pojo.Users;
import com.wangxl.licensesystem.service.UsersService;
import com.wangxl.licensesystem.utils.PageInfoUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UsersServiceImpl
 * @Description : 用户service实现类
 *
 * @Author : Wangxl
 * @Date : 2020/5/8 18:38
*/
@Service("usersService")
public class UsersServiceImpl implements UsersService {


    @Override
    public JSONObject saveOneUsersService(Users users) {
        return null;
    }

    @Override
    public JSONObject updateOneUsersService(Users users) {
        return null;
    }

    @Override
    public JSONObject deleteOneUsersService(Map<String, Object> condMap) {
        return null;
    }

    @Override
    public Users findOneUsersService(Map<String, Object> condMap) {
        return null;
    }

    @Override
    public Map<String, Object> findCondListUsersService(PageInfoUtil pageInfoUtil, Map<String, Object> condMap) {
        return null;
    }

    @Override
    public JSONObject saveOneRoleService(Role role) {
        return null;
    }

    @Override
    public JSONObject updateOneRoleService(Role role) {
        return null;
    }

    @Override
    public JSONObject deleteOneRoleService(Map<String, Object> condMap) {
        return null;
    }

    @Override
    public Role findOneRoleService(Map<String, Object> condMap) {
        return null;
    }

    @Override
    public List<Role> findCondListRoleService(PageInfoUtil pageInfoUtil, Map<String, Object> condMap) {
        return null;
    }
}
