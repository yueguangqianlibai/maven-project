package com.xtshop.controller;

import com.xtshop.common.Const;
import com.xtshop.common.ServerResponse;
import com.xtshop.pojo.User;
import com.xtshop.service.IUserService;
import com.xtshop.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @program: xtshop
 * @Date: 2019/1/4 18:10
 * @Author: river
 * @Description:
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;


    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse<String> register(String phone,String rod){
        return iUserService.register(phone,rod);
    }

    @RequestMapping(value = "send_code.do",method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse<String> sendCode(String phone){
        return iUserService.sendCode(phone);
    }


    @RequestMapping(value = "login_common.do",method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse<UserVo> loginByCommon(String username, String password, HttpSession session){
        ServerResponse<UserVo> userServerResponse = iUserService.loginByCommon(username, password);
        if (userServerResponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,userServerResponse.getData());
        }
        return userServerResponse;
    }

    @RequestMapping(value = "login_phone.do",method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse<UserVo> loginByPhone(String phone, String rod, HttpSession session){
        ServerResponse<UserVo> userServerResponse = iUserService.loginByPhone(phone,rod);
        if (userServerResponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,userServerResponse.getData());
        }
        return userServerResponse;
    }

    @RequestMapping(value = "send_code_old_phone.do",method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse<String> sendCodeOldPhone(Integer userId){
        return iUserService.sendCodeOldPhone(userId);
    }


    @RequestMapping(value = "check_verification_code.do",method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse<String> checkVerificationCode(String rod){
        return iUserService.checkVerificationCode(rod);
    }

    @RequestMapping(value = "reset_password1.do",method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse<Integer> resetPassword1(String username){
        return iUserService.resetPassword1(username);
    }

    @RequestMapping(value = "reset_password2.do",method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse<String> resetPassword2(String rod,String passwordNew,Integer userId){
        return iUserService.resetPassword2(rod, passwordNew, userId);
    }

    @RequestMapping(value = "get_information.do")
    @ResponseBody
    private ServerResponse<UserVo> getInformation(Integer userId){
        return iUserService.getInformation(userId);
    }


    @RequestMapping(value = "update_information.do")
    @ResponseBody
    private ServerResponse<User> updateInformation(User user){
        return iUserService.updateInformation(user);
    }

    @RequestMapping(value = "update_phone.do")
    @ResponseBody
    private ServerResponse<String> updatePhone(String phone,String rod,Integer userId){
        return iUserService.updatePhone(phone, rod, userId);
    }

    @RequestMapping(value = "check_admin_role.do")
    @ResponseBody
    private ServerResponse checkAdminRole(User user){
        return iUserService.checkAdminRole(user);
    }

}