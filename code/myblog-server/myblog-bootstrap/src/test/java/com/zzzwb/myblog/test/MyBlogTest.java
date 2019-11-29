package com.zzzwb.myblog.test;

import com.zzzwb.myblog.MyblogApplication;
import com.zzzwb.myblog.domain.Role;
import com.zzzwb.myblog.domain.User;
import com.zzzwb.myblog.service.RoleService;
import com.zzzwb.myblog.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 测试类
 *
 * @author zeng wenbin
 * @date Created in 2019/11/28
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyblogApplication.class)
public class MyBlogTest {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@Test
	public void test1(){
		List<User> paomian = userService.findByUserNameOrPhoneOrEmail("paomian");
		paomian.forEach(System.out::println);
		List<Role> userId = roleService.findByUserId(paomian.get(0).getId());
	}
}
