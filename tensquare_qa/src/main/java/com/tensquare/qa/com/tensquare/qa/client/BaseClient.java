package com.tensquare.qa.com.tensquare.qa.client;
import com.tensquare.base.pojo.Label;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "tensquare-base")
public interface BaseClient {

    @RequestMapping(value = "/label/{labelId}",method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String lableid);

    @RequestMapping(value = "/label/byid/{labelId}",method = RequestMethod.GET)
    public Label findId(@PathVariable("labelId") String lableid);
}
