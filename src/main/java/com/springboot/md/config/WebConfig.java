package com.springboot.md.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/9/30 上午10:11
 * @Description
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	private static final String CHAR_SET = "UTF-8";


	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		FastJsonConfig config = new FastJsonConfig();
		config.setCharset(Charset.forName(CHAR_SET));
		config.setSerializerFeatures();
		List<MediaType> mediaTypes = new ArrayList<>(18);
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
		config.setDateFormat("yyyy-MM-dd HH:mm:ss");
		config.setSerializerFeatures(SerializerFeature.WriteNonStringValueAsString, SerializerFeature.DisableCircularReferenceDetect);
		converter.setFastJsonConfig(config);
		converters.add(0, converter);

	}
}
