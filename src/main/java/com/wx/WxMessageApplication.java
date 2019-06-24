package com.wx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages= {"com.wx.dao"})
public class WxMessageApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(WxMessageApplication.class, args);
	}

}
