package secskill.error;

/**
 * @author Administrator
 * @date 2019/11/18/018 22:35
 * @Version 1.0
 */

public enum EmBusinessError implements CommonError {

    // 定义一个通用错误类型
    PARAMETER_VILIDATION_ERROR(10000,"参数不合法"),

    // 定义未知错误
    NUKNOW_ERROR(10002,"未知错误"),


    // 20000开头为用户信息相关错误定义
    USER_NOT_EXIST(10001,"用户信息不存在"),
    USER_LOGIN_FAIL(20002,"用户手机号或密码不存在"),
    USER_NOT_LOGIN(20003,"用户没有登录"),


    // 30000开头为交易信息错误定义
    STOCK_NOT_ENOUGH(30001,"库存不足")


    ;



    private int errCode;

    private String errMsg;

    private EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    /**
     * 仔细思考一下这个方法
     * @param errMsg
     * @return
     */
    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
