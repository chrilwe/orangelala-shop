package com.orangelala.service.inventory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTest {
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Test
	public void set() {
		stringRedisTemplate.boundValueOps("test1").set("test1");
		String string = stringRedisTemplate.boundValueOps("test1").get();
		System.out.println(string);
	}
	
}
