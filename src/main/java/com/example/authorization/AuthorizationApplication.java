package com.example.authorization;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.MongoCollection;
import org.jongo.Jongo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class AuthorizationApplication {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/secure/*");

		return registrationBean;
	}


	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}
}
