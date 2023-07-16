package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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



//@EnableAsync
//@SpringBootApplication
//@ComponentScan({"com.example.demo","Controller","Service","Util"})
//public class WebApplication extends SpringBootServletInitializer{
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

