package com.springboot.md.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.md.redisson.DistributedLocker;
import com.springboot.md.redisson.RedissonDistributedLocker;
import com.springboot.md.utils.RedisLockUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;

@Configuration
public class LettuceRedisConfig {

//    @Autowired
//    private RedisProperties redisProperties;


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new StringRedisTemplate(factory);
        //  RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }


    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        Config config = Config.fromYAML(new ClassPathResource("redisson-single.yml").getInputStream());
        config.setLockWatchdogTimeout(30 * 1000);
        return Redisson.create(config);
    }

    @Bean
    DistributedLocker distributedLocker(/*RedissonClient redissonClient*/) {
        DistributedLocker locker = new RedissonDistributedLocker();
        //  locker.setRedissonClient(redissonClient);
        RedisLockUtil.setLocker(locker);
        return locker;
    }


    //监听
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(new RedisExpiredListener(), new PatternTopic("__keyevent@0__:expired"));
        return container;
    }


//集群

    /**
     * 配置RedisTemplate
     * 【Redis配置最终一步】
     *
     * @param lettuceConnectionFactoryUvPv redis连接工厂实现
     * @return 返回一个可以使用的RedisTemplate实例
     */
//    @Bean
//    public RedisTemplate redisTemplate(@Qualifier("lettuceConnectionFactoryUvPv") RedisConnectionFactory lettuceConnectionFactoryUvPv) {
//        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//        template.setConnectionFactory(lettuceConnectionFactoryUvPv);
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        // key采用String的序列化方式
//        template.setKeySerializer(stringRedisSerializer);
//        // hash的key也采用String的序列化方式
//        template.setHashKeySerializer(stringRedisSerializer);
//        // value序列化方式采用jackson
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        // hash的value序列化方式采用jackson
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
//        return template;
//    }

    /*
     *//**
     * 为RedisTemplate配置Redis连接工厂实现
     * LettuceConnectionFactory实现了RedisConnectionFactory接口
     * UVPV用Redis
     *
     * @return 返回LettuceConnectionFactory
     *//*
    @Bean(destroyMethod = "destroy")
    //这里要注意的是，在构建LettuceConnectionFactory 时，如果不使用内置的destroyMethod，可能会导致Redis连接早于其它Bean被销毁
    public LettuceConnectionFactory lettuceConnectionFactoryUvPv() throws Exception {

        List<String> clusterNodes = redisProperties.getCluster().getNodes();
        Set<RedisNode> nodes = new HashSet<RedisNode>();
        clusterNodes.forEach(address -> nodes.add(new RedisNode(address.split(":")[0].trim(), Integer.valueOf(address.split(":")[1]))));
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
        clusterConfiguration.setClusterNodes(nodes);
        clusterConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        clusterConfiguration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        poolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        poolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());

        return new LettuceConnectionFactory(clusterConfiguration, getLettuceClientConfiguration(poolConfig));
    }

    *//**
     * 配置LettuceClientConfiguration 包括线程池配置和安全项配置
     *
     * @param genericObjectPoolConfig common-pool2线程池
     * @return lettuceClientConfiguration
     *//*
    private LettuceClientConfiguration getLettuceClientConfiguration(GenericObjectPoolConfig genericObjectPoolConfig) {
        *//*
        ClusterTopologyRefreshOptions配置用于开启自适应刷新和定时刷新。如自适应刷新不开启，Redis集群变更时将会导致连接异常！
         *//*
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                //开启自适应刷新
                //.enableAdaptiveRefreshTrigger(ClusterTopologyRefreshOptions.RefreshTrigger.MOVED_REDIRECT, ClusterTopologyRefreshOptions.RefreshTrigger.PERSISTENT_RECONNECTS)
                //开启所有自适应刷新，MOVED，ASK，PERSISTENT都会触发
                .enableAllAdaptiveRefreshTriggers()
                // 自适应刷新超时时间(默认30秒)
                .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(25)) //默认关闭开启后时间为30秒
                // 开周期刷新
                .enablePeriodicRefresh(Duration.ofSeconds(20))  // 默认关闭开启后时间为60秒 ClusterTopologyRefreshOptions.DEFAULT_REFRESH_PERIOD 60  .enablePeriodicRefresh(Duration.ofSeconds(2)) = .enablePeriodicRefresh().refreshPeriod(Duration.ofSeconds(2))
                .build();
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .clientOptions(ClusterClientOptions.builder().topologyRefreshOptions(topologyRefreshOptions).build())
                //将appID传入连接，方便Redis监控中查看
                //.clientName(appName + "_lettuce")
                .build();
    }*/
}
