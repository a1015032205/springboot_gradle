package com.springboot.md.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/9/29 下午4:10
 * @Description json 配置
 */
public class CommonJsonFilter {

	private static final String CHAR_SET = "UTF-8";

	/**
	 * 将所有的数据类型变成string
	 *
	 * @return
	 */
	public static Jackson2ObjectMapperBuilderCustomizer customizer() {
		return jacksonObjectMapperBuilder -> {
			jacksonObjectMapperBuilder.serializerByType(BigInteger.class, ToStringSerializer.instance);
			jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
			jacksonObjectMapperBuilder.serializerByType(long.class, ToStringSerializer.instance);
			jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance);
			jacksonObjectMapperBuilder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
		};
	}

	/**
	 * 使用fastjson对消息进行转换
	 *
	 * @return
	 */
	public static List<HttpMessageConverter> converters() {
		List<HttpMessageConverter> converters = new ArrayList<>();
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		FastJsonConfig config = new FastJsonConfig();
		config.setCharset(Charset.forName(CHAR_SET));
		config.setSerializerFeatures();
		converter.setFastJsonConfig(config);
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		mediaTypes.add(MediaType.APPLICATION_ATOM_XML);
		mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
		mediaTypes.add(MediaType.APPLICATION_PDF);
		mediaTypes.add(MediaType.APPLICATION_RSS_XML);
		mediaTypes.add(MediaType.APPLICATION_XHTML_XML);
		mediaTypes.add(MediaType.APPLICATION_XML);
		mediaTypes.add(MediaType.IMAGE_GIF);
		mediaTypes.add(MediaType.IMAGE_JPEG);
		mediaTypes.add(MediaType.IMAGE_PNG);
		mediaTypes.add(MediaType.TEXT_EVENT_STREAM);
		mediaTypes.add(MediaType.TEXT_HTML);
		mediaTypes.add(MediaType.TEXT_MARKDOWN);
		mediaTypes.add(MediaType.TEXT_PLAIN);
		mediaTypes.add(MediaType.TEXT_XML);
		converter.setSupportedMediaTypes(mediaTypes);
		converters.add(converter);
		return converters;
	}

	public static ResourceHandlerRegistry addResourceHandler(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("webjars/**").addResourceLocations("classpath:/META_INF/resources/webjars");
		return registry;
	}
}
