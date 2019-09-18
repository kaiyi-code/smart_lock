package com.tct.smart_lock.util;

import com.tct.smart_lock.domain.Result;

public class ResultUtil {
    public static Result success(int code, Object object){
        Result result = new Result();
        result.setCode(code);
        result.setMsg("success");
        result.setData(object);
        return result;
    }
    public static Result failed(int code,Object object){
        Result result = new Result();
        result.setCode(code);
        result.setMsg("failed");
        result.setData(object);
        return result;
    }
}
