package com.ftx.exportexcel.rainexcel.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/**
 * @author FanJiangFeng
 * @createTime 2021年08月31日 09:20:00
 */
@Configuration
@ComponentScan({"com.ftx.exportexcel.rainexcel.*"})
@MapperScan({"com.ftx.exportexcel.rainexcel.mapper"})
public class ExcelAutoConfigure {

}
