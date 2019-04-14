package net.sppan.base.exception;

import lombok.Data;
import net.sppan.base.common.JsonResult;
@Data
public class CustomException extends RuntimeException {

    //错误代码
    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }
    public ResultCode getResultCode(){
        return resultCode;
    }
    JsonResult jsonResult;
    public CustomException(JsonResult jsonResult){
        this.jsonResult = jsonResult;
    }

}
