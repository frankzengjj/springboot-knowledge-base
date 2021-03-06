package com.github.frankzengjj.Wiki;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan("com.github.frankzengjj")
@SpringBootApplication
@MapperScan("com.github.frankzengjj.Wiki.mapper")
@EnableScheduling
public class WikiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WikiApplication.class, args);
	}

}
