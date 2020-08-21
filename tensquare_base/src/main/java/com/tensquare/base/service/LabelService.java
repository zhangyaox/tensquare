package com.tensquare.base.service;

import com.tensquare.base.dao.labelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LabelService {
    @Autowired
    private com.tensquare.base.dao.labelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    public List<Label> findAll(){
        return labelDao.findAll();
    }

    public Label findById(String id){
        System.out.println(labelDao.findById(id));
        return labelDao.findById(id).get();
    }

    public void save(Label label){
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    public void update(Label label){
        labelDao.save(label);
    }

    public void deleteById(String id){
        labelDao.deleteById(id);
    }

    public List<Label> findSearch(Label label) {
        return labelDao.findAll(new Specification<Label>() {
            /*
            * root  就是根对象  比如我的的sql语句查找 where label.name   这个label就是root   （主要用）
            * query 其中封装有 查询的关键字  比如 order by  group by 之类的 关键字
            * ct     封装条件对象  and  like  （主要用）
            * */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder ct) {
                //我查找出来的数据  因为不确定大小 所以就先建立一个集合来存储数据
                List<Predicate> list=new ArrayList<>();

                //因为是模糊查询所以要判断是否有我模糊的这个词
                if (label.getLabelname()!=null && !"".equals(label.getLabelname())){
                    Predicate predicate= ct.like(root.get("labelname").as(String.class), "%"+label.getLabelname()+"%");//like 是 代表模糊查询   root.get("labelname") 按什么进行模糊查询从root中进行取属性   .as(String.class) 该属性的类型   相当于 where labelname like “%我get到的labelname%”
                    list.add(predicate);
                }
                if (label.getState()!=null && !"".equals(label.getState())){
                    Predicate predicate= ct.equal(root.get("state").as(String.class), label.getState());//   相当于 where labelname=我get到的state
                    list.add(predicate);
                }
                //创建一个数组来存储  我查找到的 所有数据
                Predicate[] predicate=new Predicate[list.size()];
                //把list中的数据 转换成数组格式 存入数组
                list.toArray(predicate);
                //吧我所以查找的数据 存入数组进行返回
                return ct.and(predicate);//这个and 相当于 where label.getlabelname and label.getstate
            }
        });
    }

    public Page<Label> pageQuery(Label label, int page, int size) {
        //这个结构的页数是从0开始的
        Pageable pageable= PageRequest.of(page-1,size);
        return (Page<Label>) labelDao.findAll(new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list=new ArrayList<>();
                if (label.getLabelname()!=null && !"".equals(label.getLabelname())){
                    list.add(cb.like(root.get("labelname").as(String.class),"%"+label.getLabelname()+"%"));
                }
                if (label.getState()!=null && !"".equals(label.getState())){
                    list.add(cb.equal(root.get("state").as(String.class),label.getState()));
                }
                Predicate[] predicate=new Predicate[list.size()];
                list.toArray(predicate);
                return cb.and(predicate);
            }
        },pageable);
    }
}
