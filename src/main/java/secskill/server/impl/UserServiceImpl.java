package secskill.server.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import secskill.dao.UserInfoDOMapper;
import secskill.dao.UserPasswordDOMapper;
import secskill.dataobject.UserInfoDO;
import secskill.dataobject.UserPasswordDO;
import secskill.error.BussinessException;
import secskill.error.EmBusinessError;
import secskill.server.UserService;
import secskill.server.model.UserModel;
import secskill.validator.ValidationResult;
import secskill.validator.ValidatorImpl;

/**
 * @author Administrator
 * @date 2019/11/18/018 21:23
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoDOMapper userInfoDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserInfoById(int id) {
        // 不能直接返回到前端，需要再定义一个Model,分层思想
        // Do仅仅只做一个数据库的映射
        UserInfoDO userInfoDO = userInfoDOMapper.selectByPrimaryKey(id);
        if(userInfoDO == null){
            return null;
        }
        // 通过id获取用户密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userInfoDO.getId());
        return convertFromDataObject(userInfoDO,userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BussinessException {
        if(userModel == null){
            throw new BussinessException(EmBusinessError.PARAMETER_VILIDATION_ERROR);
        }
//        if(StringUtils.isEmpty(userModel.getName())
//                ||userModel.getGender()==null
//                ||userModel.getAge()==null
//                ||StringUtils.isEmpty(userModel.getTelphone())){
//            throw new BussinessException(EmBusinessError.PARAMETER_VILIDATION_ERROR);
//        }
        ValidationResult result = validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BussinessException(EmBusinessError.PARAMETER_VILIDATION_ERROR,result.getErrMsg());
        }
        UserInfoDO userInfoDO = convertFromMode(userModel);
        try {
            // 手机号设为唯一索引，能避免重复注册
            userInfoDOMapper.insertSelective(userInfoDO);
        } catch (DuplicateKeyException e) {
            throw new BussinessException(EmBusinessError.PARAMETER_VILIDATION_ERROR,"手机号已重复注册！");
        }
        userModel.setId(userInfoDO.getId());
        UserPasswordDO userPasswordDO = convertPasswordFromMode(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);// 使用数据库默认值，数据库尽量不要出现null,使用默认值，避免空指针
    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BussinessException {
        // 通过用户的手机获取用户信息
        UserInfoDO userInfoDO = userInfoDOMapper.selectByTelphone(telphone);
        if(userInfoDO == null){
            throw new BussinessException(EmBusinessError.USER_LOGIN_FAIL,"用户或手机号不正确");
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userInfoDO.getId());
        UserModel userModel = convertFromDataObject(userInfoDO, userPasswordDO);
        if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
           throw new BussinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    /**
     * 从model转为password
     * @param userModel
     * @return
     */
    private UserPasswordDO convertPasswordFromMode(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    /**
     * 从model转为userInfoDO
     * @param userModel
     * @return
     */
    private UserInfoDO convertFromMode(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserInfoDO userInfoDO = new UserInfoDO();
        BeanUtils.copyProperties(userModel,userInfoDO);
        return userInfoDO;
    }

    private UserModel convertFromDataObject(UserInfoDO userInfoDO, UserPasswordDO userPasswordDO){
        if(userInfoDO == null){
            return null;
        }
        UserModel userModel = new UserModel();
        // 把userInfo里边的属性值copy到userModel里边，因为前者是后者的子集
        BeanUtils.copyProperties(userInfoDO,userModel);
        if(userPasswordDO != null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }
}
