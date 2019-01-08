package com.xtshop.service;

import com.xtshop.common.ServerResponse;
import com.xtshop.pojo.User;
import com.xtshop.vo.UserVo;

/**
 * @program: xtshop
 * @Date: 2019/1/4 13:01
 * @Author: river
 * @Description:
 */
public interface IUserService {

    /**
     * 用户登录1
     * @param username
     * @param password
     * @return
     */
    ServerResponse<UserVo> loginByCommon(String username, String password);

    /**
     * 用户登录2
     * @param phone
     * @param rod
     * @return
     */
    ServerResponse<UserVo> loginByPhone(String phone,String rod);

    /**
     * 用户注册
     * @param phone
     * @param rod
     * @return
     */
    ServerResponse<String> register(String phone,String rod);

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    ServerResponse<String> sendCode(String phone);

    /**
     * 给已绑定手机发送验证码
     * @param userId
     * @return
     */
    ServerResponse<String> sendCodeOldPhone(Integer userId);

    /**
     * 修改绑定手机时校验验证码
     * @param rod
     * @return
     */
    ServerResponse<String> checkVerificationCode(String rod);


    /**
     * resetPassword1
     * @param username
     * @return
     */
    ServerResponse<Integer> resetPassword1(String username);

    /**
     * resetPassword2
     * @param rod
     * @param passwordNew
     * @param userId
     * @return
     */
    ServerResponse<String> resetPassword2(String rod,String passwordNew,Integer userId);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    ServerResponse<UserVo> getInformation(Integer userId);

    /**
     *第一次修改用户信息
     * @param user
     * @return
     */
    ServerResponse<User> updateInformation(User user);

    /**
     * 一般的修改信息
     * @param user
     * @return
     */
//    ServerResponse<User> updateInformationByCommon(User user);

    /**
     * 更新手机号码
     * @param phone
     * @param rod
     * @param userId
     * @return
     */
    ServerResponse<String> updatePhone(String phone,String rod,Integer userId);

    /**
     * 检查是否管理员角色
     * @param user
     * @return
     */
    ServerResponse checkAdminRole(User user);

}
