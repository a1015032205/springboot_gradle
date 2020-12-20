package com.springboot.md.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/9/29 下午5:51
 * @Description
 */
@Configuration
@ConditionalOnExpression("true")
public class CorsDomainFilter {

	@Bean
	public CorsFilter getCorsFilter(){
		return MyCorsFilter.crosFilter();
	}
}
