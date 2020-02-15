package secskill.server.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import secskill.dao.OrderDOMapper;
import secskill.dao.SequenceDOMapper;
import secskill.dataobject.OrderDO;
import secskill.dataobject.SequenceDO;
import secskill.error.BussinessException;
import secskill.error.EmBusinessError;
import secskill.server.ItemService;
import secskill.server.OrderService;
import secskill.server.UserService;
import secskill.server.model.ItemModel;
import secskill.server.model.OrderModel;
import secskill.server.model.UserModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Administrator
 * @date 2019/11/28/028 20:17
 * @Version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId,Integer amount) throws BussinessException {

        // 1、校验订单状态、下单的商品是否存在、用户是否合法、购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if(itemModel == null){
            throw new BussinessException(EmBusinessError.PARAMETER_VILIDATION_ERROR,"商品信息不存在");
        }
        UserModel userModel = userService.getUserInfoById(userId);
        if(userModel == null){
            throw new BussinessException(EmBusinessError.PARAMETER_VILIDATION_ERROR,"用户信息不存在");
        }
        if(amount <=0|| amount>99){
            throw new BussinessException(EmBusinessError.PARAMETER_VILIDATION_ERROR,"数量信息不正确");
        }
        // 校验活动信息
        if(promoId != null){
            // 校验对应的活动是否存在这个适用商品
            if(promoId.intValue() != itemModel.getPromoModel().getId()){
                throw new BussinessException(EmBusinessError.PARAMETER_VILIDATION_ERROR,"活动信息不正确");
                // 判断活动是否正在进行中
            }else if(itemModel.getPromoModel().getStatus() != 2){
                throw new BussinessException(EmBusinessError.PARAMETER_VILIDATION_ERROR,"活动还没有开始");
            }

        }

        // 2落单减库存,冻结用户库存，使用的是数据事物,研究下事物
        boolean result = itemService.decreaceStock(itemId, amount);
        if(!result){
            throw new BussinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }
        // 3、订单入库，
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        if(promoId != null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setPromoId(promoId);
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));//BigDecimal的乘法，可以研究
        // 生成交易订单号
        orderModel.setId(this.generateOrderNo());// 重复使用sequence
        OrderDO orderDO = this.convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);
        // 增加商品的销量
        itemService.increaseSales(itemId,amount);
        // 4返回前端
        return orderModel;
    }

    private OrderDO convertFromOrderModel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        // 不同数据类型不会进行copy
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        return orderDO;
    }

    /**
     * 生成16位订单号
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 保证序列号唯一，不重复使用
    public String generateOrderNo(){
        // 时间8，序列6，分表2
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);
        // 中间序列号从数据表获取
        int sequence =0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCrrentValue();
        // 数据库数据增加步长
        sequenceDO.setCrrentValue(sequenceDO.getCrrentValue()+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        // 拼接6位
        String sequenceStr = String.valueOf(sequence);
        stringBuilder.append(sequenceStr);
        for(int i =0;i<6-sequenceStr.length();i++){// 当天数量超过6位
            stringBuilder.append(0);
        }
        // 最后两位为分库分表位,暂时写死
        stringBuilder.append("00");
        return stringBuilder.toString();
    }

}
