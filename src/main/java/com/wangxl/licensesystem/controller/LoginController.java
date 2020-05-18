package com.wangxl.licensesystem.controller;

import com.wangxl.licensesystem.pojo.Users;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @ClassName LoginController
 * @Description : 功能说明
 *
 * @Author : Wangxl
 * @Date : 2020/5/8 16:39
*/
@Controller
@Slf4j
public class LoginController {


    @RequestMapping(value = "login", method = RequestMethod.GET)
    String login(Model model) {
        model.addAttribute("user", new Users());
        log.info("#【跳往登录界面】---login()------");
        return "view/login/login";
    }
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@ModelAttribute("loginForm") Users users, RedirectAttributes redirectAttributes){
            log.info("【LoginController】 login() =====登录中=====");
        if (null == users || StringUtils.isBlank(users.getUsername()) || StringUtils.isBlank(users.getPassword())) {
            log.error("# 账号或密码错误!");
            return "login";
        }




        return "";
    }


}
