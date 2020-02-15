package secskill.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import secskill.error.BussinessException;
import secskill.error.EmBusinessError;
import secskill.response.CommonReturnType;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @date 2019/11/18/018 23:23
 * @Version 1.0
 */

public class BaseController {

    public final static String CONTENT_TYPE_FORMED ="application/x-www-form-urlencoded";// application/x-www-form-urlencoded

    /**
     * 定义exceptionhandler解决未被controller层吸收的异常
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)// 碰到Exception就进行吸收，值得研究
    @ResponseStatus(HttpStatus.OK)
    public Object HandlerException(HttpServletRequest request , Exception ex){
        // 设置需要返回的数据
        Map<String,Object> responseData = new HashMap<>();
        if(ex instanceof BussinessException){
            BussinessException bussinessException = (BussinessException) ex;
            responseData.put("errCode",bussinessException.getErrCode());
            responseData.put("errMsg",bussinessException.getErrMsg());
        }else{
            responseData.put("errCode", EmBusinessError.NUKNOW_ERROR.getErrCode());
            responseData.put("errMsg",EmBusinessError.NUKNOW_ERROR.getErrMsg());
        }
        return CommonReturnType.create(responseData,"fail");
    }

}
