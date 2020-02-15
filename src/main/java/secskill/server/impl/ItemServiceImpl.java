package secskill.server.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import secskill.dao.ItemDOMapper;
import secskill.dao.ItemStockDOMapper;
import secskill.dataobject.ItemDO;
import secskill.dataobject.ItemStockDO;
import secskill.error.BussinessException;
import secskill.error.EmBusinessError;
import secskill.server.ItemService;
import secskill.server.PromoService;
import secskill.server.model.ItemModel;
import secskill.server.model.PromoModel;
import secskill.validator.ValidationResult;
import secskill.validator.ValidatorImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @date 2019/11/25/025 19:55
 * @Version 1.0
 */

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Autowired
    private PromoService promoService;

    /**
     * 转换的ItemDo方法
     * @param itemModel
     * @return
     */
    private ItemDO convertItemDoFromItemModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);// 把model的属性copy到Do中
        // 上方法不会copy属性不同的字段
        itemDO.setPrice(itemModel.getPrice().doubleValue());// 转换类型
        return itemDO;
    }

    /**
     * 转换ItemStockDO的方法
     * @param itemModel
     * @return
     */
    private ItemStockDO convertItemStockDOFromItemModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BussinessException {
        // 校验入参
        ValidationResult result = validator.validate(itemModel);
        if(result.isHasErrors()){
            throw new BussinessException(EmBusinessError.PARAMETER_VILIDATION_ERROR,result.getErrMsg());
        }
        // 转换itemModel->dataobject
        ItemDO itemDO = this.convertItemDoFromItemModel(itemModel);
        itemDO.setId(5);
        // 写入数据库
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = this.convertItemStockDOFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList = itemDOMapper.listItem();
        List<ItemModel> itemModelList = itemDOList.stream().map(x -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(x.getId());
            ItemModel itemModel = this.convertModelFormDataObject(x, itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if(itemDO == null){
            return null;
        }
        // 获得看库存数量
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        // 将dataObject——>model
        ItemModel itemModel = this.convertModelFormDataObject(itemDO, itemStockDO);
        // 获取活动商品信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if(promoModel != null && promoModel.getStatus() != 3){
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaceStock(Integer itemId, Integer amount) throws BussinessException {
        int affectedRow = itemStockDOMapper.decreaseStock(itemId, amount);
        if(affectedRow>0){
            // 更新库存成功
            return true;
        }else{
            // 更新库存失败
            return false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BussinessException {
        // 增加销量也需要控制在一个事物内
        itemDOMapper.increaseSales(itemId,amount);
    }

    /**
     * 转化为模型的方法
     * @param itemDO
     * @param itemStockDO
     * @return
     */
    private ItemModel convertModelFormDataObject(ItemDO itemDO,ItemStockDO itemStockDO){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
}
