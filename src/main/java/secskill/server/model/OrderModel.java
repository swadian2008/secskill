package secskill.server.model;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @date 2019/11/28/028 19:59
 * @Version 1.0
 */
// 用户下单交易模型
public class OrderModel {

    private String id;

    // 购买的用户id
    private Integer userId;

    // 购买的商品的id
    private Integer itemId;

    // 秒杀商品方式下单id
    private Integer promoId;

    // 购买商品的单价,若promoid非空，表示秒杀商品价格
    private BigDecimal itemPrice;

    // 购买的数量
    private Integer amount;

    // 购买金额
    private BigDecimal orderPrice;

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}
