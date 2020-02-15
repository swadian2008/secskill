package secskill.dao;

import org.apache.ibatis.annotations.Param;
import secskill.dataobject.ItemStockDO;

public interface ItemStockDOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ItemStockDO record);

    int insertSelective(ItemStockDO record);

    ItemStockDO selectByPrimaryKey(Integer id);

    ItemStockDO selectByItemId(Integer itemId);

    int updateByPrimaryKeySelective(ItemStockDO record);

    int updateByPrimaryKey(ItemStockDO record);

    /**
     * 删减库存
     * @param itemId
     * @param amount
     * @return
     */
    int decreaseStock(@Param("itemId") Integer itemId,@Param("amount") Integer amount);
}