package com.orangelala.service.shopcart;

import java.util.Iterator;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@org.junit.Test
	public void test() {
		stringRedisTemplate.opsForHash().put("test_", "test01", "value");
		stringRedisTemplate.opsForHash().put("test_", "test02", "value02");
		Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("test");
		for(Iterator<Object> iterator = entries.keySet().iterator();iterator.hasNext();) {
			String key = (String) iterator.next();
			System.out.println(entries.get(key));
		}
	}
}
