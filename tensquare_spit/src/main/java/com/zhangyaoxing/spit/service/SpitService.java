package com.zhangyaoxing.spit.service;

import com.zhangyaoxing.spit.dao.SpitDao;
import com.zhangyaoxing.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional//添加事务
public class SpitService {
    @Autowired
    SpitDao spitDao;
    @Autowired
    MongoTemplate mongoTemplate;

    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    public Spit save(Spit spit){
        //先添加默认的一些数据
        //判断该吐槽有没有父节点 如果有父节点就给他的 父节点 吐槽数加1
        if(spit.getParentid()!=null&&"".equals(spit.getParentid())){//在写一些东西前都要判断是否为空  就是判断是否有父节点
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));//查找的是 _id为父节点的
            Update update = new Update().inc("thumbup",1);//让 thumbup 加1
            mongoTemplate.updateFirst(query,update,"spit");
        }
        return spitDao.save(spit);
    }

    public void deleteById(String id){
         spitDao.deleteById(id);
    }

    public Spit updateById(Spit spit){
        return spitDao.save(spit);
    }

    public Page<Spit> findByParentid(String parentid, int page,int size){
        PageRequest of = PageRequest.of(page, size);
        //为了防止重复点赞 在redis中存储东西  到时候先查找redis的数据
        return spitDao.findByParentid(parentid,of);
    }
}
