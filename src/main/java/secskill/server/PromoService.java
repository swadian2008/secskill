package secskill.server;

import secskill.server.model.PromoModel;

public interface PromoService {

    /**
     * 通过商品Id获取秒杀信息(即将或者将要秒杀的活动信息)
     * @param itemId
     * @return
     */
    PromoModel getPromoByItemId(Integer itemId);

}
