package secskill.controller;

import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import secskill.controller.viewmodel.UserVO;
import secskill.error.BussinessException;
import secskill.error.EmBusinessError;
import secskill.response.CommonReturnType;
import secskill.server.UserService;
import secskill.server.model.UserModel;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author Administrator
 * @date 2019/11/18/018 21:24
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*") // 跨域请求，防止ajax跨域请求报错标签，值得研究
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;// 其中有threadLocal可以处理每个线程自己的任务

    @RequestMapping(value="/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType login(@RequestParam(name = "telphone") String telphone,
                                  @RequestParam(name = "password") String password) throws BussinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(password)|| org.apache.commons.lang3.StringUtils.isEmpty(telphone)){
            throw new BussinessException(EmBusinessError.PARAMETER_VILIDATION_ERROR);
        }
        // 用户登录服务，用来校验用户登录是否合法
        UserModel userModel = userService.validateLogin(telphone, this.EncodeByMd5(password));
        // 将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        return CommonReturnType.create(null);
    }

    /**
     * 用户获取opt短信接口
     */
    @RequestMapping(value="/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType getotp(@RequestParam(name="telphone") String telphone){
        // 需要按照一定规则生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);
        // 将otp验证码同对应的用户手机关联,(使用httpSession)
        httpServletRequest.getSession().setAttribute(telphone,otpCode);
        // 将otp验证码通过短信通道发送给用户
        System.out.println("telphone="+telphone+" and otpcode="+otpCode);
        return CommonReturnType.create(null);
    }

    /**
     *  用户注册接口
     */
    @RequestMapping(value="/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType register(@RequestParam(name="telphone") String telphone,
                                     @RequestParam(name="otpCode") String otpCode,
                                     @RequestParam(name="name") String name,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password) throws BussinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 验证手机号和短信验证码
        String sessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if(!StringUtils.equals(otpCode,sessionOtpCode)){
            throw new BussinessException(EmBusinessError.PARAMETER_VILIDATION_ERROR,"短信验证码不正确");
        }
        // 注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelphone(telphone);// 设置唯一索引
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(password));// 使用MD5算法进行加密
        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    /**
     * 密码进行加密
     * @param str
     * @return
     */
    private String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 确定计算的方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        // 加密字符串
        String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    @RequestMapping("/get")
    public CommonReturnType getUserInfo(@RequestParam(name="id") Integer id) throws BussinessException {
        UserModel user = null;//userService.getUserInfoById(id);
        // 模型转换
        if(user == null){
            throw new BussinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserVO userVO = convertFromModel(user);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        // 把userModel里边的属性copy到userVO里边
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }


}
