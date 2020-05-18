package com.wangxl.licensesystem.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
/**
 * @ClassName AuthorizingRealmImpl
 * @Description : 登录验证，授权，Realm
 *
 * @Author : Wangxl
 * @Date : 2020/5/8 16:59
*/
@Slf4j
public class AuthorizingRealmImpl extends AuthorizingRealm {

    /**
     * @MethodName : doGetAuthorizationInfo
     * @Description : 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     * @Param :
     * @Return : org.apache.shiro.authz.AuthorizationInfo
     * @Author : Wangxl
     * @Date : 2020/5/8 17:00
    */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("【AuthorizationInfo doGetAuthorizationInfo() 】");

        return null;
    }
    /**
     * @MethodName : doGetAuthenticationInfo
     * @Description : 认证回调函数,登录时调用
     * @Param :
     * @Return : org.apache.shiro.authc.AuthenticationInfo
     * @Author : Wangxl
     * @Date : 2020/5/8 17:00
    */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("【AuthenticationInfo doGetAuthenticationInfo 】");

        return null;
    }
}
