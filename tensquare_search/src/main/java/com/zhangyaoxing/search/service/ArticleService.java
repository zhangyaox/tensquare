package com.zhangyaoxing.search.service;

import com.tensquare.common.domain.StatusCode;
import com.zhangyaoxing.search.dao.ArticleDao;
import com.zhangyaoxing.search.pojo.Article;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Service
@Transactional
public class ArticleService {
    @Autowired
    ArticleDao articleDao;

    public Article save(Article article){
        return articleDao.save(article);
    }

    public Page<Article> findByKey(String key,int page,int size){
        PageRequest of = PageRequest.of(page-1, size);
        System.out.println(key);
        return articleDao.findByTitleOrContentLike(key,key,of);
    }
}
