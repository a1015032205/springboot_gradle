package com.springboot.md;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class DemoApplicationTests {


    public static void main(String[] args) {

        List<Map<String, Object>> arrayList = CollUtil.newArrayList();

        Map<String, Object> map1 = MapUtil.newHashMap();
        map1.put("AAA", "111");
        map1.put("BBB", Arrays.asList("123", "2222"));
        arrayList.add(map1);
        Map<String, Object> map2 = MapUtil.newHashMap();
        map2.put("AAA", "23454354");
        map2.put("BBB", Arrays.asList("4444", "678990"));
        arrayList.add(map2);
        arrayList.stream().flatMap(x -> MapUtil.get(x, "BBB", List.class).stream()).collect(Collectors.toList());


        List bbb = arrayList.stream().
                map(x -> MapUtil.get(x, "BBB", List.class))
                .reduce(new ArrayList<>(), (all, item) -> {
                    all.addAll(item);
                    return all;
                });


        System.out.println(bbb);

        Stream.iterate(0, n -> n + 1)
                .limit(10)
                .forEach(x -> System.out.println(x));

        Stream.iterate(new int[]{0, 1}, n -> new int[]{n[1], n[0] + n[1]})
                .limit(20)
                .map(n -> n[0])
                .forEach(x -> System.out.println(x));


        /**
         * 从Stream中依次获取满足条件的元素，直到不满足条件为止结束获取
         *
         * 举例：Stream中的元素 12, 4, 3, 6, 8, 9
         *
         * 条件是 x -> x % 2 == 0 ，即判断是否为偶数，即当遇到元素不为偶数时终止获取
         *
         * 那么得到的结果输出就是，12, 4 因为下一个元素为3不为偶数，即结束获取，丢弃后面的其他元素
         */
        IntStream intStream = IntStream.of(12, 4, 3, 6, 8, 9);
     //   intStream.takeWhile(x -> x % 2 == 0).forEach(System.out::print);

        /**
         * 从Stream中依次删除满足条件的元素，直到不满足条件为止结束删除
         *
         * 举例：Stream中的元素 12, 4, 3, 6, 8, 9
         *
         * 条件是 x -> x % 2 == 0 ，即判断是否为偶数，即当遇到元素不为偶数时终止删除
         *
         * 那么得到的结果输出就是，3, 6, 8, 9 因为下一个元素为3不为偶数，即结束删除，返回3及以后剩下的所有元素
         */
     //   IntStream.of(12, 4, 3, 6, 8, 9).dropWhile(x -> x % 2 == 0).forEach(System.out::print);
    }


}
