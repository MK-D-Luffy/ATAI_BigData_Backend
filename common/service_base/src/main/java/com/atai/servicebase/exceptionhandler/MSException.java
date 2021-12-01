package com.atai.servicebase.exceptionhandler;

import com.atai.commonutils.result.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//自定义异常
@Data
@AllArgsConstructor  //生成有参数构造方法
@NoArgsConstructor   //生成无参数构造
public class MSException extends RuntimeException {
    private Integer code;//状态码
    private String message;//异常信息

    public MSException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public MSException(ResultCodeEnum resultCodeEnum) {
        this.message = resultCodeEnum.getMessage();
        this.code = resultCodeEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MSException{" +
                "message=" + this.getMessage() +
                ", code=" + code +
                '}';
    }
}