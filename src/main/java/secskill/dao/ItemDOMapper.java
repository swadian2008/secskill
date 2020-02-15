package secskill.dao;

import org.apache.ibatis.annotations.Param;
import secskill.dataobject.ItemDO;

import java.util.List;

public interface ItemDOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ItemDO record);

    int insertSelective(ItemDO record);

    ItemDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemDO record);

    int updateByPrimaryKey(ItemDO record);

    List<ItemDO> listItem();

    int increaseSales(@Param("id") Integer id,@Param("amount") Integer amount);
}