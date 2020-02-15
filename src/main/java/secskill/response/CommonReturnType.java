package secskill.response;

/**
 * @author Administrator
 * @date 2019/11/18/018 22:20
 * @Version 1.0
 */

public class CommonReturnType {

    // 表明对应的请求的返回的处理结果“success”或fail
    private String status;

    // 若status=success,则data内返回前端所需要的json数据
    // 若status=fail,则data内使用通用的错误码格式
    private Object data;

    // 定义一个通用的创建方法
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);// 状态
        type.setData(result); // 数据
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
