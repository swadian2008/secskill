package secskill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import secskill.controller.BaseController;
import secskill.error.BussinessException;
import secskill.error.EmBusinessError;
import secskill.response.CommonReturnType;
import secskill.server.OrderService;
import secskill.server.model.OrderModel;
import secskill.server.model.UserModel;

import javax.servlet.http.HttpServletRequest;

import static secskill.controller.BaseController.CONTENT_TYPE_FORMED;

/**
 * @author Administrator
 * @date 2019/11/29/029 20:35
 * @Version 1.0
 */
@RestController
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    // 封装下单请求
    @RequestMapping(value="/createorder",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType creteOrder(@RequestParam(name = "itemId") Integer itemId,
                                       @RequestParam(name = "promoId", required = false) Integer promoId,
                                       @RequestParam(name = "amount") Integer amount) throws BussinessException {
        // 获取用户的登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BussinessException(EmBusinessError.USER_NOT_LOGIN,"用户未登录，不能下单");
        }
        // 获取登录用户的信息
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel orderModel = orderService.createOrder(userModel.getId(),itemId,promoId,amount);
        return CommonReturnType.create(null);
    }


}
