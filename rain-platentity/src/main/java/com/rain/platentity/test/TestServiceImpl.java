package com.rain.platentity.test;

import com.rain.platentity.anotation.EncryptDecryptClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TestServiceImpl.java
 * @Description TODO
 * @createTime 2021年01月26日 14:51:00
 */
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    TestDao testDao;
    @Override
    public int insertEmp(Test test) {
        return testDao.insertEmp(test);
    }

    @Override
    public List<Test> selectEmp() {
        return testDao.selectEmp();
    }
}
