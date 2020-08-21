package com.tensquare.user.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	JwtUtil jwtUtil;


	@RequestMapping(value = "login",method = RequestMethod.POST)
	public Result userLogin(@RequestBody User user){
		User user1 = userService.userLogin(user.getMobile(), user.getPassword());
		if (user1==null){
			return new Result(false,StatusCode.LOCINERROR,"用户登录失败");
		}
		//用户登录 给他一个用户的 token
		String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
		HashMap<String, Object> map = new HashMap<>();
		map.put("token",token);
		map.put("roles","user");
		return new Result(true,StatusCode.OK,"用户登录成功",map);
	}

	@RequestMapping(value = "sendsms/{mobile}",method = RequestMethod.POST)//短信验证
	public Result sendSms(@PathVariable String mobile){//手机号
		userService.sendSms(mobile);
		return new Result(true,StatusCode.OK,"短信验证成功");
	}

	@RequestMapping(value = "register/{code}",method = RequestMethod.POST)//用户注册
	public Result regist(@PathVariable String code,@RequestBody User user){//
		//先根据我 之前取的短信验证码时传入 redis的 redis的key 得到我存入redis的验证码 用这个验证码和 code(用户传递的验证码)进行比对
		String num = (String) redisTemplate.opsForValue().get("checkcode" + user.getMobile());
		if (num.isEmpty()){//判断非空  isempty方法 num有内容返回 false 没有返回true
			//如果是true就说明redis没有值  就报个异常
			return new Result(false,StatusCode.ERROR,"先获取手机验证码");
		}
		if (num.equals(code)){//redis中的验证码 和 用户输入的验证码相等 可以注册
			userService.add(user);
			return new Result(true,StatusCode.OK,"用户注册成功");
		}else {
			System.out.println("用户存入"+code+"redis中的"+num);
			return new Result(false,StatusCode.ERROR,"先输入正确的手机验证码");
		}

	}
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
