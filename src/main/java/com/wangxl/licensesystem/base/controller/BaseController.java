package com.wangxl.licensesystem.base.controller;

import com.wangxl.licensesystem.utils.BaseUtils;
import com.wangxl.licensesystem.utils.VerifyCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @ClassName BaseController
 * @Description : controller父类
 *
 * @Author : Wangxl
 * @Date : 2020/5/8 9:58
*/
@Controller
@Slf4j
@RequestMapping("/base")
public class BaseController {

    /**
     * @author: Wangxl
     * @description: 用来生成验证码
     * @date :2019年3月16日 下午3:15:27
     * @modifier:
     * @modificationTime:
     * @description:
     */
    @RequestMapping("/randImg")
    public void randImg(HttpServletRequest request, HttpServletResponse response){
        BaseUtils.ClearCache(response);
        response.setContentType("image/jpeg");

        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        // 存入会话session
        HttpSession session = request.getSession(true);
        session.setAttribute("rand", verifyCode);
        // 生成图片
        int w = 200,h = 55;
        try
        {
            VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
        } catch (IOException e)
        {
            log.error("randImg case Exception",e);
        }
    }

    public static boolean getValue(String fieldName)throws Exception{
        try{
            Field field=BaseController.class.getField(fieldName);
            return (boolean)field.get(null);
        }
        catch (Exception e) {
            throw new IllegalArgumentException( " 找不到相关的变量: " + e.getMessage());
        }

    }

}
