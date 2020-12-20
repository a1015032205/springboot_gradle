package com.springboot.md.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/9/14 下午2:43
 * @Description
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

	public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

	/**
	 * 针对redis数据失效事件，进行数据处理
	 * @param message
	 * @param pattern
	 */
	@Override
	public void onMessage(Message message, byte[] pattern) {
		// 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
		String expiredKey = message.toString();
		System.out.println(expiredKey);
	}
}