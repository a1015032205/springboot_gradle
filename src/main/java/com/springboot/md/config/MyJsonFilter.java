package com.springboot.md.config;

import lombok.Setter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.List;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/9/29 下午5:58
 * @Description
 */
@Configuration
@Setter
public class MyJsonFilter {

	@Bean
	@Primary
	public Jackson2ObjectMapperBuilderCustomizer customizer() {
		return CommonJsonFilter.customizer();
	}


	@Bean
	public List<HttpMessageConverter> converters(){
		return CommonJsonFilter.converters();
	}

}
