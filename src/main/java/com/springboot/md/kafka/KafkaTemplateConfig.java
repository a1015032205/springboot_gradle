package com.springboot.md.kafka;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;

import java.util.Map;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-11-02 22:04
 * @Description: KafkaTemplateConfig
 */

//@EnableKafka
//@Configuration
public class KafkaTemplateConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean(name = "defaultKafkaTemplate")
    public KafkaTemplate<?, ?> kafkaTemplate(@Qualifier("defaultKafkaProducerFactory") ProducerFactory<Object, Object> kafkaProducerFactory,
                                             ProducerListener<Object, Object> kafkaProducerListener,
                                             ObjectProvider<RecordMessageConverter> messageConverter) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        kafkaTemplate.setProducerListener(kafkaProducerListener);
        kafkaTemplate.setDefaultTopic(kafkaProperties.getTemplate().getDefaultTopic());
        return kafkaTemplate;
    }

    @Bean
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<>();
    }

    @Bean(name = "defaultKafkaProducerFactory")
    public ProducerFactory<Object, Object> kafkaProducerFactory() {
        DefaultKafkaProducerFactory<Object, Object> factory = new DefaultKafkaProducerFactory<>(
                kafkaProperties.buildProducerProperties());
        String transactionIdPrefix = kafkaProperties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        return factory;
    }

    /**
     * 获取生产者工厂
     */
    @Bean(name = "newKafkaProducerFactory")
    public ProducerFactory<Object, Object> newProducerFactory() {
        Map<String, Object> producerProperties = kafkaProperties.buildProducerProperties();
        // 修改参数名称
        producerProperties.put(ProducerConfig.ACKS_CONFIG, "all");
        DefaultKafkaProducerFactory<Object, Object> factory = new DefaultKafkaProducerFactory<>(
                producerProperties);
        String transactionIdPrefix = kafkaProperties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }

        return factory;
    }

    @Bean(name = "newKafkaTemplate")
    public KafkaTemplate<?, ?> newKafkaTemplate(@Qualifier("newKafkaProducerFactory") ProducerFactory<Object, Object> kafkaProducerFactory,
                                                ProducerListener<Object, Object> kafkaProducerListener,
                                                ObjectProvider<RecordMessageConverter> messageConverter) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        kafkaTemplate.setProducerListener(kafkaProducerListener);
        kafkaTemplate.setDefaultTopic(kafkaProperties.getTemplate().getDefaultTopic());
        return kafkaTemplate;
    }
}
