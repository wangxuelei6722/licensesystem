package com.wangxl.licensesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class LicensesystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicensesystemApplication.class, args);
    }

}
