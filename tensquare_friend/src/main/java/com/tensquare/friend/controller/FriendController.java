package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    HttpServletRequest request;
    @Autowired
    FriendService friendService;

    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid,@PathVariable String type){
        //验证登录  获取 用户信息
        Claims claims= (Claims) request.getAttribute("claims_user");//我 只要用户的权限
        if (claims==null){//只要不是用户权限就不可以 使用
            return new Result(false, StatusCode.LOCINERROR,"权限不足");
        }
//      判断状态非空  判断状态 是 喜欢 还是 不喜欢
        //然后 先 获取用户的id
        String id = claims.getId();

        if (type!=null){
            if (type=="1"){//喜欢
                int flag=friendService.addFriend(id,friendid);//返回 int类型是可能有多种情况  不好用 布尔类型
                // 喜欢 和 不喜欢 都不可以重复的点击  返回 0 重复了   1 成功   2失败
                if (flag==0){
                    return new Result(false, StatusCode.ERROR,"不可以重复点击");
                }else if (flag==1){
                    return new Result(false, StatusCode.OK,"添加成功");
                }else {
                    return new Result(false, StatusCode.ERROR,"添加失败");
                }
            }else {//不喜欢
                //friendService.addNoFriend();
            }
        }else {
            return new Result(false, StatusCode.ERROR,"参数异常");
        }
        return new Result();
    }
}
