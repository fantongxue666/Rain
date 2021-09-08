package com.ftx.exportexcel.rainexcel.model;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @createTime 2021年08月30日 14:09:00
 *
 * 参数传递对象
 */
public class ParamsModel {

    //对象参数，对象放在这里 参数会被拆开
    private Object objParams;

    //对象列表参数
    private List<?> objListParams;

    //Map参数 Map<String 参数名,Object 参数值>  （存储单个的参数，包括对象，对象存放在这里会作为一个完整的对象）
    private Map mapParams;

    //Map列表
    List<Map> mapListParams;

    public List<Map> getMapListParams() {
        return mapListParams;
    }

    public void setMapListParams(List<Map> mapListParams) {
        this.mapListParams = mapListParams;
    }

    public Object getObjParams() {
        return objParams;
    }

    public void setObjParams(Object objParams) {
        this.objParams = objParams;
    }

    public List<?> getObjListParams() {
        return objListParams;
    }

    public void setObjListParams(List<?> objListParams) {
        this.objListParams = objListParams;
    }

    public Map getMapParams() {
        return mapParams;
    }

    public void setMapParams(Map mapParams) {
        this.mapParams = mapParams;
    }
}


