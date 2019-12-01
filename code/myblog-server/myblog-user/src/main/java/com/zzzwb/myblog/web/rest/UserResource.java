package com.zzzwb.myblog.web.rest;

import com.zzzwb.myblog.annotation.Access;
import com.zzzwb.myblog.bean.SearchBean;
import com.zzzwb.myblog.constant.Action;
import com.zzzwb.myblog.domain.User;
import com.zzzwb.myblog.domain.UserRole;
import com.zzzwb.myblog.function.OnlineUserFunction;
import com.zzzwb.myblog.service.UserRoleService;
import com.zzzwb.myblog.service.UserService;
import com.zzzwb.myblog.web.dto.BaseDto;
import com.zzzwb.myblog.web.vo.AddRoleVo;
import com.zzzwb.myblog.web.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户资源接口
 *
 * @author zeng wenbin
 * @date Created in 2019/7/28
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理接口")
public class UserResource extends BaseResource<User> {
	/**
	 * 用户事务层
	 */
	private UserService userService;
	/**
	 * 用户角色事务层
	 */
	private UserRoleService userRoleService;


	public UserResource(UserService userService, UserRoleService userRoleService) {
		super(userService);
		this.userService = userService;
		this.userRoleService = userRoleService;
	}

	@PutMapping("/login")
	@ApiOperation("用户登陆")
	public ResponseEntity<BaseDto> login(@RequestBody LoginVo loginVo){
		String username = loginVo.getUsername();
		List<User> users = this.userService.findByUserNameOrPhoneOrEmail(username);
		if (users == null || users.size() == 0){
			return  ResponseEntity.ok(BaseDto.error("账号/电话/邮箱不存在!"));
		} else if (users.size() == 1){
			User user = users.get(0);
			String voPassword = Md5Crypt.md5Crypt(loginVo.getPassword().getBytes(), "$1$" + user.getUsername());
			if (user.getPassword().equals(voPassword)) {
				//生成token
				String token = Md5Crypt.md5Crypt(UUID.randomUUID().toString().getBytes());
				//查出用户的角色并将用用户存储进redis在线用户列表中
				SearchBean searchBean = new SearchBean("userId", user.getId());
				List<UserRole> userRoles = userRoleService.queryBySearch(searchBean);
				//给用户的roles字段赋值
				setRolesToUser(user, userRoles);
				token = "myBlog-" + token;
				//存储用户在线信息
				OnlineUserFunction.addOnlineUser(token, user);
				return ResponseEntity.ok(BaseDto.success(token));
			} else {
				return ResponseEntity.ok(BaseDto.error("密码错误！"));
			}
		} else {
			if (loginVo.getUsername().equals(users.get(0).getEmail())) {
				return  ResponseEntity.ok(BaseDto.error("此邮箱被多人使用，登陆失败！"));
			} else {
				return  ResponseEntity.ok(BaseDto.error("此电话被多人使用，登陆失败!"));
			}
		}
	}

	@PostMapping("/regist")
	@ApiOperation("注册")
	public ResponseEntity<BaseDto> regist(@RequestBody User user){
		//todo
		return ResponseEntity.ok(BaseDto.success());
	}

	@PutMapping("/addRole")
	@ApiOperation("给用户添加角色")
	@Access(value = Action.OTHER,description = "给用户添加角色")
	public ResponseEntity<BaseDto> addRole(AddRoleVo vo){
		//todo
		return ResponseEntity.ok(BaseDto.success());
	}

	@GetMapping("/Logout")
	@ApiOperation("登出")
	@Access(value = Action.OTHER,description = "登出")
	public ResponseEntity logOut(HttpServletRequest request){
		String token = request.getHeader("token");
		OnlineUserFunction.removeUserByToken(token);
		return ResponseEntity.ok().build();
	}

	/**
	 * 想用户对象设置roles字段
	 *
	 * @param user 用户
	 * @param userRoles 用户对应的角色集合
	 */
	private void setRolesToUser(User user, List<UserRole> userRoles){
		StringBuilder builder = new StringBuilder();
		for (UserRole userRole : userRoles) {
			builder.append(userRole.getRoleId() + ",");
		}
		user.setRoles(builder.substring(0, builder.length()-1));
	}
}
