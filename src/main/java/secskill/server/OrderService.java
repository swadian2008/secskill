package secskill.server;

import secskill.error.BussinessException;
import secskill.server.model.OrderModel;

/**
 * @author Administrator
 * @date 2019/11/28/028 20:16
 * @Version 1.0
 */

public interface OrderService {

    // 创建订单
    // 通过前端Url上传过来的秒杀活动id,下单校验对应的id是否属于对应商品，且活动已经开始
    OrderModel createOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BussinessException;

}
