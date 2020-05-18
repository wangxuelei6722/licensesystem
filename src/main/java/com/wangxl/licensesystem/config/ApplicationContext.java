package com.wangxl.licensesystem.config;

import com.wangxl.licensesystem.utils.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.Calendar;

/**
 * @ClassName ApplicationContext
 * @Description : 功能说明 将version版本号写入application中，给css,js引用时用
 *
 * @Author : Wangxl
 * @Date : 2020/5/8 16:38
*/
@Component
@Slf4j
public class ApplicationContext implements ServletContextAware {


    @Override
    public void setServletContext(ServletContext context) {

        String datetime = DateFormatUtil.dateToString(Calendar.getInstance().getTime(), DateFormatUtil.FM_yyyyMMddHHmmssSSS);
        String contextPath = context.getContextPath();
        log.info("# version={} , contextPath={}", datetime, contextPath);
        context.setAttribute("version_css", datetime);
        context.setAttribute("version_js", datetime);
        context.setAttribute("rootPath", contextPath);
    }

}