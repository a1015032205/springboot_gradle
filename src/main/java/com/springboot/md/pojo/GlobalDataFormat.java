package com.springboot.md.pojo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2021/1/11 2:27 下午
 * @Description
 */
@Slf4j
@ControllerAdvice
public class GlobalDataFormat implements ResponseBodyAdvice {

	private static final String REGEX = "&";
	private static final String WSTR = "万";
	private static final String ESTR = "亿";
	private static final BigDecimal W = new BigDecimal(100000);
	private static final BigDecimal E = new BigDecimal(1000000000);

	private static StringBuffer sb = new StringBuffer();

	@Autowired
	private HttpServletRequest httpServletRequest;


	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		try {
			sb.setLength(0);
			//标志位  是否开启格式化
			String format = "1";
			if ("0".equals(format)) {
				return body;
			}
			String method = httpServletRequest.getMethod();
			String requestURI = httpServletRequest.getRequestURI();
			//通过url+method拿到Map<String，GlobalFormat>
			Map<String, GlobalFormat> map = MapUtil.newHashMap();


			if (MapUtil.isEmpty(map)) {
				return body;
			}

			boolean empty = ObjectUtil.isEmpty(body);//||body instanceof ResponseError

			return empty ? body : judgment(body, map);
		} catch (Exception e) {
			log.error("[{}]", e);
			return ex(body, e);
		}

	}

	private Object ex(Object body, Exception e) {
		Map<String, Object> map = (Map) body;
		map.put("ex", ExceptionUtil.stacktraceToString(e));
		if (sb.length() > 0) {
			String[] split = sb.toString().split(REGEX);
			String last = CollUtil.getLast(Arrays.asList(split));
			map.put("sb", last);
		}
		return map;
	}

	private Object judgment(Object body, Map<String, GlobalFormat> map) {
		if (body instanceof String || body instanceof Number) {
			return body;
		} else if (body instanceof Collection) {
			return formatCollection((Collection) body, map);
		} else {
			return formatMap(BeanUtil.toBean(body, Map.class), map);
		}
	}

	private Object formatMap(Map<String, Object> body, Map<String, GlobalFormat> map) {
		for (Map.Entry<String, Object> entry : body.entrySet()) {
			Object value = entry.getValue();
			if (doCondition(map, entry)) {
				GlobalFormat globalFormat = BeanUtil.toBean(map.get(entry.getKey()), GlobalFormat.class);
				Integer format = globalFormat.getFormat();
				Integer num = globalFormat.getNum();
				Integer deci = globalFormat.getDeci();
				String unit = globalFormat.getUnit();
				Integer isChinese = globalFormat.getIsChinese();

				//是否开始特殊处理
				String other = "0";
				if ("1".equalsIgnoreCase(other) && flag(entry)) {
					continue;
				}

				if (num == 1) {
					entry.setValue(deci(value, deci, isChinese));
				} else if (format == 1) {
					entry.setValue(getFormat(value, deci, unit, isChinese));
				} else {
					//只处理保留小数
					String obj = processObj(value);
					BigDecimal div = NumberUtil.div(new BigDecimal(obj), 1, deci, RoundingMode.HALF_UP);
					entry.setValue(div);
				}
			} else {
				entry.setValue(judgment(value, map));
			}
		}
		return body;
	}


	private boolean flag(Map.Entry<String, Object> entry) {
		if (getSize(entry) && sb.length() > 0) {
			String[] split = sb.toString().split(REGEX);
			//遍历当前字段名
			String s = split[split.length - 1];
			char[] chars = s.toCharArray();
			ArrayList<String> objects = CollUtil.newArrayList();
			for (char aChar : chars) {
				String s1 = String.valueOf(aChar);
				objects.add(s1);
			}
			String method = httpServletRequest.getMethod();
			String requestURI = httpServletRequest.getRequestURI();
			//	G1+method+url
			//字段包含那些需要跳过的规则  &分割
			String str = "";
			String[] strings = str.split(REGEX);
			ArrayList<String> strings1 = CollUtil.newArrayList(strings);

			Collection<String> intersection = CollUtil.intersection(objects, strings1);
			return CollUtil.isEmpty(intersection);


		}
		return false;
	}


	/**
	 * 是否有配置规则
	 *
	 * @param entry
	 * @return
	 */
	private boolean getSize(Map.Entry<String, Object> entry) {
		String method = httpServletRequest.getMethod();
		String requestURI = httpServletRequest.getRequestURI();
		//通过method+url拿到配置的字段 多个用&隔开
		String cacheDataBykey = "";
		if (ObjectUtil.isNotEmpty(cacheDataBykey)) {
			String[] split = cacheDataBykey.split(REGEX);
			ArrayList<String> strings = CollUtil.newArrayList(split);
			return strings.contains(entry.getKey());
		}
		return false;
	}

	private boolean doCondition(Map<String, GlobalFormat> map, Map.Entry<String, Object> entry) {
		String key = entry.getKey();
		Object value = entry.getValue();
		boolean flag = map.containsKey(key) && (value instanceof String || value instanceof Number);
		if (!flag) {
			sb.append(key).append(REGEX);
		}
		return flag;
	}

	private Object formatCollection(Collection body, Map<String, GlobalFormat> map) {
		List list = CollUtil.newArrayList(body);
		Object first = CollUtil.getFirst(list);
		GlobalFormat globalFormat = BeanUtil.toBean(map.values(), GlobalFormat.class);
		if (ObjectUtil.isNotEmpty(first)) {
			if (first instanceof String || first instanceof Number) {
				for (int i = 0; i < list.size(); i++) {
					list.set(i, getFormat(list.get(i), globalFormat.getDeci(), globalFormat.getUnit(), globalFormat.getIsChinese()));
				}
			} else if (first instanceof Collection) {
				for (int i = 0; i < list.size(); i++) {
					list.set(i, formatCollection((Collection) list.get(i), map));
				}
			} else {
				for (int i = 0; i < list.size(); i++) {
					list.set(i, formatMap(JSONUtil.toBean(JSONUtil.parseObj(list.get(i)), Map.class), map));
				}
			}
		}
		return list;
	}

	private Object getFormat(Object data, int deci, String unit, int isChinese) {
		String obj = processObj(data);
		//绝对值
		double abs = Math.abs(Double.parseDouble(obj));
		BigDecimal bigDecimal = new BigDecimal(abs);
		BigDecimal decimal = new BigDecimal(obj);
		//a<0 小于w
		int a = bigDecimal.compareTo(W);
		//b<0 小于e
		int b = bigDecimal.compareTo(E);
		return getResult(deci, unit, isChinese, decimal, a, b);
	}

	private Object getResult(int deci, String unit, int isChinese, BigDecimal bigDecimal, int a, int b) {
		//显示固定单位
		if (StringUtils.isNotBlank(unit)) {
			return checkUnit(deci, unit, isChinese, bigDecimal);
		} else {
			//小于w
			if (a < 0) {
				BigDecimal div = NumberUtil.div(bigDecimal, 1, deci, RoundingMode.HALF_UP);
				BigDecimal decimal = NumberUtil.div(bigDecimal, 1, 0, RoundingMode.HALF_UP);
				return String.valueOf(bigDecimal).contains(".") ? div : decimal;
			} else if (b > 0) {//大于e
				BigDecimal divide = bigDecimal.divide(E, deci, RoundingMode.HALF_UP);
				return isChinese == 0 ? divide : divide + ESTR;
			} else {//大于w 小于e
				BigDecimal divide = bigDecimal.divide(W, deci, RoundingMode.HALF_UP);
				return isChinese == 0 ? divide : divide + WSTR;
			}
		}
	}


	private Object checkUnit(int deci, String unit, int isChinese, BigDecimal bigDecimal) {
		if ("w".equalsIgnoreCase(unit)) {
			BigDecimal divide = bigDecimal.divide(W, deci, BigDecimal.ROUND_HALF_UP);
			return isChinese == 0 ? divide : divide + WSTR;
		}
		BigDecimal divide = bigDecimal.divide(E, deci, BigDecimal.ROUND_HALF_UP);
		return isChinese == 0 ? divide : divide + ESTR;

	}

	/**
	 * 计算百分比
	 *
	 * @param data
	 * @param deci
	 * @param isChinese
	 * @return
	 */
	private Object deci(Object data, int deci, int isChinese) {
		BigDecimal bigDecimal = new BigDecimal(processObj(data));
		BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
		BigDecimal div = NumberUtil.div(multiply, 1, deci, RoundingMode.HALF_UP);
		return isChinese == 0 ? div : div + "%";
	}

	private String processObj(Object obj) {
		return ObjectUtil.isEmpty(obj) ? "0.00" : String.valueOf(obj);
	}
}
