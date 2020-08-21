package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{
	//热门  等待  最新
    @Query(value = "SELECT * FROM tb_problem a LEFT JOIN tb_pl b ON a.id=b.problemid WHERE b.labelid=? ORDER BY a.reply DESC",nativeQuery = true)//nativeQuery = true 意味着我前面可以写sql语句了
    public Page<Problem> hotList(String labelid, Pageable pageable);
    @Query(value = "SELECT * FROM tb_problem a LEFT JOIN tb_pl b ON a.id=b.problemid WHERE b.labelid=? and a.reply=0 ORDER BY a.replytime DESC",nativeQuery = true)//nativeQuery = true 意味着我前面可以写sql语句了
    public Page<Problem> waitList(String labelid, Pageable pageable);
    @Query(value = "SELECT * FROM tb_problem a LEFT JOIN tb_pl b ON a.id=b.problemid WHERE b.labelid=? ORDER BY a.replytime DESC",nativeQuery = true)//nativeQuery = true 意味着我前面可以写sql语句了
    public Page<Problem> newList(String labelid, Pageable pageable);

}
