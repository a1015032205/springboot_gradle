package com.springboot.md.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2021/1/11 2:37 下午
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlobalFormat {

	private Long id;
	private String url;
	private String method;
	private String group;
	private String field;


	private Integer format;
	private Integer num;
	private Integer deci;
	private String unit;
	private Integer isChinese;

}
