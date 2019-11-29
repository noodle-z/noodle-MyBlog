package com.zzzwb.myblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 启动类
 *
 * @author zeng wenbin
 * @date Created in 2019/7/28
 */
@MapperScan(basePackages = "com.zzzwb.myblog.mapper")
@SpringBootApplication
@ServletComponentScan
public class MyblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyblogApplication.class, args);
	}
}
