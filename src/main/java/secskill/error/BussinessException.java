package secskill.error;

/**
 * @author Administrator
 * @date 2019/11/18/018 22:45
 * @Version 1.0
 */
// 包装器模式
public class BussinessException extends Exception implements CommonError {

    private CommonError commonError;

    // 直接接收传参用于构造业务异常
    public BussinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    // 接收自定义errMsg的方式构造业务异常
    public BussinessException(CommonError commonError,String errMsg){
        super();
        this.commonError = commonError;
        // 自定义异常信息，二次改写
        this.commonError.setErrMsg(errMsg);
    }


    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
