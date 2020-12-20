package com.springboot.md.controller;

import com.springboot.md.config.AbstracController;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-11-02 21:56
 * @Description: kafka
 */
//@RestController
//@Slf4j
//@RequestMapping("/KafkaController")
public class KafkaController extends AbstracController {

  //  @Resource
    private KafkaTemplate<String, String> kafkaTemplate;


    // 发送消息
    @GetMapping("/kafka/normal/{message}")
    public void sendMessage1(@PathVariable("message") String normalMessage) {
        kafkaTemplate.send("topic1", normalMessage);
    }



}
