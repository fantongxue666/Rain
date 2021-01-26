package com.rain.platentity.test;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TestService.java
 * @Description TODO
 * @createTime 2021年01月26日 14:51:00
 */
public interface TestService {
    int insertEmp(Test test);
    List<Test> selectEmp();
}
