package com.springboot.md.demo;

import cn.hutool.core.collection.CollUtil;

import java.util.*;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/12/30 4:06 下午
 * @Description
 */
public class Java8 {
	public static void main(String[] args) {
		List<Map<String, List<Integer>>> list = new ArrayList<>();

		Map<String, List<Integer>> map1 = new HashMap(8);
		map1.put("a", Arrays.asList(1, 2));
		map1.put("b", Arrays.asList(2, 4));
		map1.put("c", Arrays.asList(1, 2, 3, 4));

		Map<String, List<Integer>> map2 = new HashMap(8);
		map2.put("a", Arrays.asList(2, 3, 4));
		map2.put("c", Arrays.asList(1, 2, 8));
		map2.put("d", Arrays.asList(3, 4, 5));

		Map<String, List<Integer>> map3 = new HashMap(8);
		map3.put("c", Arrays.asList(5, 6));
		map3.put("d", Arrays.asList(3, 6, 7, 8));
		map3.put("e", Arrays.asList(8, 9, 12));

		list.add(map1);
		list.add(map2);
		list.add(map3);


		list.stream().reduce(new HashMap<>(16), (o1, o2) -> {
			Set<String> key1 = (Set<String>) o1.keySet();
			Set<String> key2 = (Set<String>) o2.keySet();
			System.out.println(key1);
			System.out.println(key2);
			Collection<String> intersection = CollUtil.intersection(key1, key2);
			return null;
		});


	}
}
