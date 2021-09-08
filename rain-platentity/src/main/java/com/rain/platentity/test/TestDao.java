package com.rain.platentity.test;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TestDao.java
 * @Description TODO
 * @createTime 2021年01月26日 09:26:00
 */
@Mapper
@Repository
public interface TestDao {

    @Select(value = "select * from seller;")
    List<Test> selectEmp();

    @Insert(value = "insert into seller(name,account,password) values(#{name},#{account},#{password})")
    int insertEmp(Test test);
}
