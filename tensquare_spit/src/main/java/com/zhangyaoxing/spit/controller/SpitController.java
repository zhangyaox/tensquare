package com.zhangyaoxing.spit.controller;

import com.zhangyaoxing.spit.pojo.Spit;
import com.zhangyaoxing.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    SpitService spitService;
    @Autowired
    MongoTemplate mongoTemplate;


    @RequestMapping(method = RequestMethod.POST)
    public void save(@RequestBody Spit spit){
        System.out.println(spit);
        spitService.save(spit);
        new Result(false, StatusCode.ERROR, "添加成功");
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
       return new Result(false, StatusCode.ERROR, "查看成功",spitService.findAll());
    }

    @RequestMapping(value = "{spitId}",method = RequestMethod.GET)//测试
    public Result findById(@PathVariable String spitId){
        System.out.println(spitId);
        return new Result(false, StatusCode.ERROR, "查看成功",spitService.findById(spitId));
    }

//    @RequestMapping(value = "{spitId}",method = RequestMethod.POST)
//    public Result findById(Spit spit){
//        Spit spit = spitService.updateById(spitId);
//        return new Result(false, StatusCode.ERROR, "查看成功");
//    }

    @RequestMapping(value = "{spitId}",method = RequestMethod.DELETE)//测试
    public Result deleteById(@PathVariable String spitId){
        spitService.deleteById(spitId);
        return new Result(false, StatusCode.ERROR, "删除成功");
    }

    @RequestMapping(value = "comment/{parentid}/{page}/{size}",method = RequestMethod.GET)//测试
    public Result findByParentid(@PathVariable String parentid,@PathVariable int page,@PathVariable int size){
        Page<Spit> byParentid = spitService.findByParentid(parentid, page, size);
        return new Result(false, StatusCode.ERROR, "分页查找成功",new PageResult<Spit>(byParentid.getTotalElements(),byParentid.getContent()));
    }

    @RequestMapping(value = "thumbup/{spitId}",method = RequestMethod.GET)//测试
    public void thumbup(@PathVariable String spitId){
//        Spit byId = spitService.findById(spitId);  这样子写效率低
//        if(byId.getThumbup()!=null){
//            byId.setThumbup(byId.getThumbup()+1);
//        }else {
//            byId.setThumbup(0);
//        }
//
//        spitService.save(byId);

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));//查找的是 _id为1的
        Update update = new Update().inc("thumbup",1);//让 thumbup 加1
        mongoTemplate.updateFirst(query,update,"spit");

//        return new Result(false, StatusCode.ERROR, "点赞成功");
    }
}
