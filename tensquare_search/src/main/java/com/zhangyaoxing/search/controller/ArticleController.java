package com.zhangyaoxing.search.controller;

import com.tensquare.common.domain.StatusCode;
import com.zhangyaoxing.search.pojo.Article;
import com.zhangyaoxing.search.service.ArticleService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {
    @Autowired
    ArticleService articleService;

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody  Article article){
        Article article1 = article;
        articleService.save(article1);
        return new Result(true,StatusCode.OK,"es添加成功");
    }

    @RequestMapping(value = "/{key}/{page}/{size}",method = RequestMethod.GET)
    public Result findByKey(@PathVariable String key,@PathVariable int page,@PathVariable int size){
        Page<Article> byKey = articleService.findByKey(key, page, size);
        System.out.println(byKey.getTotalElements()+"--"+byKey.hasContent());
        return new Result(true, StatusCode.OK,"查询成功",new PageResult<Article>(byKey.getTotalElements(),byKey.getContent()));
    }
}
