package com.springboot.md.controller;

import com.springboot.md.config.AbstracController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-11-07 1:06
 * @Description:
 */

@RestController
@RequestMapping("/DemoController")
public class DemoController extends AbstracController {

    @GetMapping("/getDemo")
    public Object getDemo() {
        return setSuccessModelMap("HELLO WORLD!");
    }
}
