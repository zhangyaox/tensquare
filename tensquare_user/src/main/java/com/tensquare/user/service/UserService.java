package com.tensquare.user.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import util.JwtUtil;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private IdWorker idWorker;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	RabbitTemplate rabbitTemplate;
	@Autowired
	RedisTemplate redisTemplate;
	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	HttpServletRequest httpServletRequest;//用来获取请求头

	public User userLogin(String mobile,String password){//因为管理员少可以用姓名查找 用户可能有重名的所以是电话查找
		User byNickname = userDao.findByMobile(mobile);
		System.out.println("mobile="+mobile+"--password="+password+"--byNickname="+byNickname.getPassword()+"--encoder="+encoder.matches(byNickname.getPassword(),password));
		if (byNickname!=null&&encoder.matches(password,byNickname.getPassword())){
			return byNickname;
		}else {
			return null;
		}
	}


	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 增加 用户的注册
	 * @param user
	 */
	public void add(User user) {
		user.setId( idWorker.nextId()+"" );
		//对注册的用户的密码进行加密
		user.setPassword(encoder.encode(user.getPassword()));

		user.setFollowcount(0);//关注
		user. setFanscount(0);//粉丝数
		user.setOnline (0L);//在线时长
		user. setRegdate(new Date());//册日期
		user. setUpdatedate (new Date());//更新日期
		user.setLastdate(new Date());//最后登陆日期
		userDao.save(user);
	}

	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除   在删除用户时  要判断他是不是有这个权限 只有有管理员权限(admin角色)才可以进行删除用户
	 * @param id
	 */
	public void deleteById(String id) {
//		//前后端约定:前端请求微服务时需要添加头信息Authorization,内容为Bearer+空格+token  按约定走
//		String authorization = httpServletRequest.getHeader("Authorization");
//		System.out.println("authorization==="+authorization);
//		if (authorization==null&&"".equals(authorization)){
//			throw new RuntimeException("权限不够");
//		}
//		if (!authorization.startsWith("Bearer ")){//内容为Bearer+空格+token  startsWith("Bearer ")判断是不是以Bearer+空格开头
//			throw new RuntimeException("权限不够");
//		}
//		//前面都过了 表示非空 格式正确  先取到token
//		String token = authorization.substring(7);//从第7个开始截取  就是Bearer 后面的
//		try {//解析token  防止有人乱写token
//			User user = userDao.findById(id).get();
//			Claims claims = jwtUtil.parseJWT(token);
//			String roles = (String) claims.get("roles");//按用户名 得到用户  然后判断这个用户是不是空 是不是admin(管理员)
//			if (roles==null&&!roles.equals("admin")){
//				throw new RuntimeException("权限不够");
//			}
//		}catch (Exception e){
//			throw new RuntimeException("权限不够");//如果Claims claims = jwtUtil.parseJWT(token); 有异常表名token有错误
//		}
		//该方法需要 管理员权限 所以调用request中的权限看看当前是用户还是管理员  管理员运行 用户不可以
		String token = (String) httpServletRequest.getAttribute("claims_admin");
		if (token==null&&"".equals(token)){//非空 就是查找成功  该权限是管理员 空不是管理员
			throw new RuntimeException("权限不够");
		}
		userDao.deleteById(id);//上面的都通过了 那么就是admin  可以进行删除操作
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

    public void sendSms(String mobile) {
		//前台判断非空 格式 后台返回 短信验证码
		/*<dependency>
		  <groupId>org.apache.commons</groupId>
		  <artifactId>commons-lang3</artifactId>
	  </dependency>  pom 用这个apc 的 随机数*/
		String s = RandomStringUtils.randomNumeric(6);//这个6表示随机是随机的6位
		// 首先我向我的redis 之类的先存储一份 用来进行验证  用户发一份  控制台输出一份用来验证
		//首先在redis存储一份  设置过期时间 5分钟
		redisTemplate.opsForValue().set("checkcode"+mobile,s,5, TimeUnit.MINUTES);
		System.out.println(redisTemplate.opsForValue().get("checkcode" + mobile));
		//用户发一份  用户的消息发送到 mq中 靠mq来进行调用第三方发送短信
		Map<String, String> map = new HashMap<>();
		map.put("mobile",mobile);
		map.put("num",s);
		System.out.println(mobile+"==="+s);
		rabbitTemplate.convertAndSend("sms",map);
		//控制台输出一份用来验证
		System.out.println("手机"+mobile+"验证码是"+s);
	}
}
