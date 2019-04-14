package net.sppan.base.exception;


import com.google.common.collect.ImmutableMap;
import net.sppan.base.common.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常捕获类
 * @author Administrator
 * @version 1.0
 * @create 2018-09-14 17:32
 **/
@ControllerAdvice//控制器增强
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

  /*  //定义map，配置异常类型所对应的错误代码
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    //定义map的builder对象，去构建ImmutableMap
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();
*/

    //捕获Exception此类异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResult exception(Exception exception){
        //记录日志
        LOGGER.error("catch exception:{}",exception.getMessage());
        if(exception instanceof NullPointerException){
            return new JsonResult(99,exception.getMessage(),null);

        }
        if(exception instanceof IllegalArgumentException){
            return new JsonResult(99,exception.getMessage(),null);

        }
            //返回99999异常
         return JsonResult.failure(exception.getMessage());
        }


    }

/*    static {
        //定义异常类型所对应的错误代码
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
        builder.put(NullPointerException.class,CommonCode.NUll_INVALID_PARAM);

    }*/

