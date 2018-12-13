package com.domi.service.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Results {


    private Map<String, Object> result;

    public Results() {
        this.result = new HashMap<String, Object>();
    }


    public static Results of() {
        return new Results();
    }

    public static Map<String, Object> results(int state,String info,String method,Object data) {
        return  of().put("errcode", state).put("info", info).put("method", method)
                .put("data", data).toMap();
    }
    /*成功*/
    public static Map<String, Object> SUCCES(String method,Object data) {
        return results(0,"成功",method,data);
    }
    /*空集合__个数*/
    public static Map<String, Object> EMPTY(String method) {
        return results(0,"成功",method, Results.of().put("data",Collections.EMPTY_MAP)
                .put("size",0).toMap());
    }
    /*未登录*/
    public static Map<String, Object> NOLOGIC() {
        return results(2,
                "cookie值不存在，请重新登录！", "", Collections.EMPTY_MAP);
    }
    /*请求参数有误*/
    public static Map<String, Object> PARS_ERROR() {
        return results(11,
                "请求参数有误!!", "", Collections.EMPTY_MAP);
    }
    /*操作失败*/
    public static Map<String, Object> FAILED(String method) {
        return results(1,"操作失败",method, Results.of().put("data",Collections.EMPTY_MAP)
                .put("size",0).toMap());
    }
    /*异常*/
    public static Map<String, Object> ERROR(String method) {
        return results(10,"出问题了，程序猿们等着今晚被捡肥皂了 @_@",method, Results.of().put("data",Collections.EMPTY_MAP)
                .put("size",0).toMap());
    }

    public Results put(String name, Object value) {
        this.result.put(name, value);
        return this;
    }

    public Map<String, Object> toMap() {
        return this.result;
    }

}
