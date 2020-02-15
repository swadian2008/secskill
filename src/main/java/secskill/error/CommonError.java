package secskill.error;

/**
 * @author Administrator
 * @date 2019/11/18/018 22:32
 * @Version 1.0
 */

public interface CommonError {

    public int getErrCode();

    public String getErrMsg();

    public CommonError setErrMsg(String errMsg);
}
