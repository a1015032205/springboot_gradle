package com.springboot.md.demo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-11-05 13:30
 * @Description: 扫雷
 */

public class Game {
    public static void main(String[] args) {
        matrix();
    }

    public static void matrix() {
        System.out.println("==================矩阵如下：==============================");
        List<Map<String, Object>> list = CollUtil.newArrayList();
        //雷的坐标  11==》X1Y1
        Collection<Integer> random = getRandom();
        for (int i = 9; i >= 1; i--) {
            for (int j = 1; j <= 9; j++) {
                String index = StrUtil.join("", String.valueOf(j), String.valueOf(i));
                String str = " X" + j + "Y" + i + " ";
                System.out.print(str);
                Map<String, Object> map = MapUtil.newHashMap();
                //坐标数字
                int num = Integer.parseInt(index);
                //拿到8面的坐标
                List<Integer> nice = getNice(num);
                //提示数字
                int size = CollUtil.intersectionDistinct(nice, random).size();
                if (random.contains(num)) {
                    map.put(str, "✖");
                } else {
                    map.put(str, size);
                }
                list.add(map);
            }
            System.out.println();
        }
        System.out.println("==================随机生成10个雷的XY坐标： ==============================");
        System.out.println(random);
        System.out.println("==================UI说明如下：数字说明本身没有雷，周边一圈有雷的个数， ✖:说明有雷 ==============================");
        int k = 0;
        for (int j = 0; j < list.size(); j++) {
            for (int i = 0; i <= 8; i++) {
                if (k <= list.size() - 1) {
                    Map<String, Object> obj = list.get(k++);
                    System.out.print("   " + obj + "  ");
                }
            }
            System.out.println();
        }
    }

    public static Collection<Integer> getRandom() {
        return Stream.generate(() -> RandomUtil.randomInt(11, 100)).filter(x -> x % 10 != 0 && x > 10 && x < 100).limit(10).collect(Collectors.toSet());
    }

    public static List<Integer> getNice(Integer index) {
        //X5Y5
        //上面 X5Y6 ==>Y+1                     V+1
        //下面 X5Y4 ==>Y-1                     V-1
        //左边 X4Y5 ==>X-1                     V-10
        //右面 X6Y5 ==>X+1                     V+10
        //左上角 X4Y6 ==>X-1  Y+1              V-9
        //左下角 X4Y4 ==>X-1  Y-1              V-11
        //右上角 X6Y6 ==>X+1  Y+1              V+11
        //右下角 X6Y4 ==>X+1  Y-1              V+9
        //总结： 左：X-1  右：X+1  上：Y+1  下：Y-1
        // 看当前坐标的8个方向中有几个雷  就是提示雷的数字
        return CollUtil.newArrayList(index + 1, index - 1, index - 10, index + 10, index - 9, index - 11, index + 11, index + 9);
    }
}
