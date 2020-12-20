package com.springboot.md;

import cn.hutool.core.date.StopWatch;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-10-26 21:34
 * @Description:
 */

@SpringBootTest
public class demo {
    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            list1.add(i + "");
        }
        for (int i = 0; i < 100; i++) {
            list2.add(i + "1");
        }
        list2.add("6584");
        d1(list1, list2);
        d2(list2, list1);
        d3(list1, list2);
        d4(list2, list1);
    }

    public static void d1(List<String> list1, List<String> list2) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                if (list1.get(i).equals(list2.get(j))) {
                    System.out.print("");
                }
            }
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis() + "D1");
    }


    public static void d2(List<String> list1, List<String> list2) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                if (list1.get(i).equals(list2.get(j))) {
                    System.out.print("");
                }
            }
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis() + "D2");
    }

    public static void d3(List<String> list1, List<String> list2) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        list1.parallelStream().filter(list2::contains).map(System.out::printf).collect(Collectors.toList());
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis() + "D3");
        System.out.println(stopWatch.getTotalTimeMillis() + "D3");
    }

    public static void d4(List<String> list1, List<String> list2) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        list2.parallelStream().filter(list1::contains).map(System.out::printf).collect(Collectors.toList());
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis() + "D4");
        System.out.println(stopWatch.getTotalTimeMillis() + "D4");
    }
}
