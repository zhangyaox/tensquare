package com.tensquare.base.comtroller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin//
@RequestMapping("/label")
public class BaseController {
    @Autowired
    private LabelService labelServicel;

    @RequestMapping(value = "/byid/{labelId}",method = RequestMethod.GET)
    public Label findId(@PathVariable("labelId") String lableid){
        return labelServicel.findById(lableid);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",labelServicel.findAll());
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String lableid){
        System.out.println("==="+lableid);
        return new Result(true, StatusCode.OK,"查询成功",labelServicel.findById(lableid));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label){
        labelServicel.save(label);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.PUT)
    public Result update(@PathVariable("labelId") String lableid,@RequestBody Label label){
        label.setId(lableid);
        labelServicel.update(label);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.DELETE)
    public Result deleteByIds(@PathVariable("labelId") String lableid){
        labelServicel.deleteById(lableid);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @RequestMapping(value = "search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Label label){
        List<Label> list=labelServicel.findSearch(label);
        return new Result(true, StatusCode.OK,"查询成功",list);
    }

    @RequestMapping(value = "search/{page}/{size}",method = RequestMethod.POST)
    public Result pageQuery(@RequestBody Label label,@PathVariable int page,@PathVariable int size){
        Page<Label> pagelabel =labelServicel.pageQuery(label,page,size);
        return new Result(true, StatusCode.OK,"查询成功",new PageResult<Label>(pagelabel.getTotalElements(),pagelabel.getContent()));//pagelabel.getTotalElements()总数量,pagelabel.getContent()数据
    }
}
