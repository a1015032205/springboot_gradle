package com.springboot.md.controller;

import com.springboot.md.config.AbstracController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-11-02 21:56
 * @Description: kafka
 */
@RestController
@Slf4j
@RequestMapping("/KafkaController")
public class KafkaController extends AbstracController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    //发送消息
    @GetMapping("/kafka/normal/{message}")
    public void sendMessage1(@PathVariable("message") String normalMessage) {
        for (int i = 0; i < 10000; i++) {
            kafkaTemplate.send("topic1", normalMessage + i);
        }

    }


}
