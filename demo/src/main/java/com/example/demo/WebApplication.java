package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class WebApplication{

	public static void main(String[] args) {

		SpringApplication.run(WebApplication.class, args);
	}
}


//@Configuration
//@EnableAsync
//@EnableScheduling
//@SpringBootApplication
//@ComponentScan({"com.example.demo","Controller","Service","Util"})
//public class WebApplication extends SpringBootServletInitializer {
////public class WebApplication{
//
//	public static void main(String[] args) {
//
//		SpringApplication.run(WebApplication.class, args);
//	}
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(WebApplication.class);
//	}
//}

