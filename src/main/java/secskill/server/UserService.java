package secskill.server;

import secskill.dataobject.UserInfoDO;
import secskill.error.BussinessException;
import secskill.server.model.UserModel;

public interface UserService {

    UserModel getUserInfoById(int id);

    void register(UserModel userModel) throws BussinessException;

    /**
     * 用户注册的手机
     * 用户加密后的密码
     * @param telphone
     * @param encrptPassword
     * @throws BussinessException
     */
    UserModel validateLogin(String telphone,String encrptPassword) throws BussinessException;
}
