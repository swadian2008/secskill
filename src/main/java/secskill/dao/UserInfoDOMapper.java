package secskill.dao;

import secskill.dataobject.UserInfoDO;

public interface UserInfoDOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserInfoDO record);

    int insertSelective(UserInfoDO record);

    UserInfoDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfoDO record);

    int updateByPrimaryKey(UserInfoDO record);

    UserInfoDO selectByTelphone(String telphone);

}