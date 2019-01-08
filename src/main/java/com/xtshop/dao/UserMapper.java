package com.xtshop.dao;

import com.xtshop.pojo.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author river
 */
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 查询是否存在该用户
     * @param username
     * @return
     */
    int selectUsername(String username);

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    User selectLogin(@Param("username") String username,@Param("password") String password);

    /**
     * 查询用户名是否重复
     * @param username
     * @return
     */
    int selectUserByUsername(String username);

    /**
     * 查询邮箱是否重复
     * @param email
     * @return
     */
    int selectUserByEmail(String email);

    /**
     * 通过userId查
     * @param email
     * @param userId
     * @return
     */
    int checkEmailByUserId(@Param("email") String email,@Param("userId") Integer userId);

    /**
     * 查phone通过id
     * @param userId
     * @return
     */
    String selectPhoneByUserId(Integer userId);

    /**
     * 通过name查id
     * @param userName
     * @return
     */
    int selectUserIdByUsername(String userName);

    /**
     * 通过id改密码
     * @param passwordNew
     * @param userId
     * @return
     */
    int updatePasswordByUserId(@Param("passwordNew") String passwordNew,@Param("userId") Integer userId);

    /**
     * 查userByPhone
     * @param phone
     * @return
     */
    int selectPhone(String phone);

    /**
     * 根据phone查出User对象
     * @param phone
     * @return
     */
    User selectUserByPhone(String phone);

}