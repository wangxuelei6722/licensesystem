package com.wangxl.licensesystem.service;

import com.wangxl.licensesystem.dao.RoleDao;
import com.wangxl.licensesystem.dao.UsersDao;
import com.wangxl.licensesystem.pojo.Role;
import com.wangxl.licensesystem.pojo.Users;
import com.wangxl.licensesystem.utils.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UsersServiceTest {

    @Resource
    private UsersDao usersDao;
    @Resource
    private RoleDao roleDao;
    @Autowired
    private EncryptUtil encryptUtil;

    @Test
    public void saveOneUsersService() {
        Users users = new Users();
        users.setRoleId(1);
        users.setNickname("wangxl");
        users.setPassword(encryptUtil.encodeStr("123456"));
        users.setUsername("wangxl");
        users.setSalt("555555");
        users.setEmail("475524154@qq.com");
        users.setPhone("19200000000");
        users.setLoginCount(0);
        users.setFailedCount(0);
        users.setAddress("北京市海淀区学院路35号");
        users.setStatus(Byte.valueOf("1"));
        users.setCreateTime(new Date());
        users.setUpdateTime(new Date());
        int result = usersDao.save(users);
        Assert.assertNotNull(result);
    }

    @Test
    public void updateOneUsersService() {
        Map<String,Object> condMap = new HashMap<String, Object>();
        condMap.put("id",2);
        Users users = (Users) usersDao.findOne(condMap);;
        users.setRoleId(1);
        users.setNickname("wangxuelei");
        users.setUsername("wangxuelei");
        users.setPhone("19200000009");
        int result = usersDao.update(users);
        Assert.assertEquals(1,result);

    }

    @Test
    public void deleteOneUsersService() {
        Map<String,Object> condMap = new HashMap<String, Object>();
        condMap.put("nickname","wangxuelei");
        Users users = (Users) usersDao.findOne(condMap);
        condMap.put("id",users.getId());
        int result = usersDao.delete(condMap);
        Assert.assertEquals(1,result);
    }

    @Test
    public void findOneUsersService() {
        Map<String,Object> condMap = new HashMap<String, Object>();
        condMap.put("nickname","wangxl");
        condMap.put("roleId",1);
        Users users = (Users) usersDao.findOne(condMap);
        Assert.assertNotNull(users);
    }

    @Test
    public void findCondListUsersService() {
        Map<String,Object> condMap = new HashMap<String, Object>();
        condMap.put("status",1);
        List<Users> usersList = usersDao.findList(condMap);
        Assert.assertNotEquals(usersList.size(),0);


    }

    @Test
    public void saveOneRoleService() {
        Role role = new Role();
        role.setCreateId(1);
        role.setUpdateId(1);
        role.setName("普通用户");
        role.setContent("普通用户");
        role.setLevel(Byte.valueOf("3"));
        role.setStatus(Byte.valueOf("1"));
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        role.setPubTime(new Date());
        int result = roleDao.save(role);
        Assert.assertNotNull(result);

    }

    @Test
    public void updateOneRoleService() {
        //先查询，在更新。
        Map<String,Object> condMap = new HashMap<String, Object>();
        condMap.put("id",2);
        Role role = (Role) roleDao.findOne(condMap);
        role.setName("管理员");
        role.setContent("管理员");
        int result = roleDao.update(role);
        Assert.assertEquals(1,result);

    }

    @Test
    public void deleteOneRoleService() {
        //根据ID删除
        Map<String,Object>  condMap = new HashMap<String, Object>();
        condMap.put("id",3);
        int result = roleDao.delete(condMap);
        Assert.assertEquals(1,result);
    }

    @Test
    public void findOneRoleService() {
        Map<String,Object> condMap = new HashMap<String, Object>();
        condMap.put("id",1);
        Role role = (Role) roleDao.findOne(condMap);
        Assert.assertNotNull(role);
    }

    @Test
    public void findCondListRoleService() {
        //根据状态查询角色列表
        Map<String,Object> condMap = new HashMap<String, Object>();
        condMap.put("status",1);
        List<Role> roleList = roleDao.findList(condMap);
        Assert.assertNotEquals(0,roleList.size());
    }
}
