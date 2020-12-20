package com.springboot.md.controller;

import cn.hutool.core.map.MapUtil;
import com.springboot.md.config.AbstracController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-11-30 22:52
 * @Description:
 */

@RestController
@RequestMapping("/testController")
public class TestController extends AbstracController {

    @RequestMapping("/json")
    public void demo(@RequestBody HashMap<String, Object> map) {
        Map<String, Object> employee = MapUtil.get(map, "employee", Map.class);
        List<Map<String, Object>> list = MapUtil.get(employee, "feeds", List.class);
        Map<String, Object> objectMap = list.get(0);
        System.out.println(objectMap.get("id"));
    }
}
