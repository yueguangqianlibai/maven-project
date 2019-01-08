package com.xtshop.service.impl;

import com.xtshop.common.Const;
import com.xtshop.common.ServerResponse;
import com.xtshop.dao.UserMapper;
import com.xtshop.pojo.User;
import com.xtshop.service.IUserService;
import com.xtshop.util.DateTimeUtil;
import com.xtshop.util.MD5Util;
import com.xtshop.util.PhoneCodeUtil;
import com.xtshop.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: xtshop
 * @Date: 2019/1/4 13:04
 * @Author: river
 * @Description:
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    public UserServiceImpl() {
        super();
    }

    @Override
    public ServerResponse<UserVo> loginByCommon(String username, String password) {
        int resultCount = userMapper.selectUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("未找到该用户!");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码输入错误!");
        }
        user.setPassword(StringUtils.EMPTY);
        UserVo userVo = new UserVo();
        userVo = assembleUserToUservo(user);
        return ServerResponse.createBySuccess("登录成功!", userVo);
    }

    @Override
    public ServerResponse<UserVo> loginByPhone(String phone, String rod) {
        int resultCount = userMapper.selectPhone(phone);
        if (resultCount > 0) {
            System.err.println(phone);
            System.err.println(rod);
            System.err.println(PhoneCodeUtil.phoneCode);
            System.err.println(this.determineIfTheCaptchaIsCorrect(rod));
            if (this.determineIfTheCaptchaIsCorrect(rod) == 0) {
                User user = userMapper.selectUserByPhone(phone);
                user.setPassword(StringUtils.EMPTY);
                UserVo userVo = new UserVo();
                userVo = assembleUserToUservo(user);
                return ServerResponse.createBySuccess("登录成功!", userVo);
            }else{
                return ServerResponse.createByErrorMessage("验证码输入错误!");
            }
        }
        return ServerResponse.createByErrorMessage("未找到该用户!");

    }

    @Override
    public ServerResponse<String> register(String phone, String rod) {
        //判断验证码输入正确
        if (this.determineIfTheCaptchaIsCorrect(rod) == 0) {
            User user = new User();
            user.setUsername(generateRandomString());
            user.setPhone(phone);
            user.setEmail(getEmail(6, 13));
            user.setRole(Const.Role.ROLE_CUSTOMER);
            int resultCount = userMapper.insert(user);
            if (resultCount == 0) {
                return ServerResponse.createByErrorMessage("注册失败!");
            }
            return ServerResponse.createBySuccessMessage("注册成功!");
        }
        return ServerResponse.createByErrorMessage("验证码不正确!");
    }

    @Override
    public ServerResponse<String> sendCode(String phone) {
        PhoneCodeUtil.phoneCode = null;
        if (phone != null) {
            PhoneCodeUtil.getCode(phone);
            return ServerResponse.createBySuccessMessage("验证码发送成功!");
        }
        return ServerResponse.createByErrorMessage("验证码发送失败!");
    }

    @Override
    public ServerResponse<String> sendCodeOldPhone(Integer userId) {
        String oldPhone = userMapper.selectPhoneByUserId(userId);
        if (oldPhone == null) {
            return ServerResponse.createByErrorMessage("发送验证码失败!");
        }
        this.sendCode(oldPhone);
        return ServerResponse.createBySuccessMessage("发送验证码成功!");
    }

    @Override
    public ServerResponse<String> checkVerificationCode(String rod) {
        if (this.determineIfTheCaptchaIsCorrect(rod) == 0) {
            return ServerResponse.createBySuccessMessage("验证通过!");
        }
        return ServerResponse.createByErrorMessage("验证码输入不正确!");
    }

    @Override
    public ServerResponse<Integer> resetPassword1(String username) {
        //1 输入用户名
        //2 查询旧手机
        //3 发送验证码
        //4 验证判断
        //5 保存新密码
        int resultCount = userMapper.selectUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("该用户未注册!");
        }
        Integer userId = userMapper.selectUserIdByUsername(username);
        this.sendCodeOldPhone(userId);
        return ServerResponse.createBySuccess("发送验证码成功!", userId);
    }

    @Override
    public ServerResponse<String> resetPassword2(String rod, String passwordNew, Integer userId) {
        if (this.determineIfTheCaptchaIsCorrect(rod) == 0) {
            String password = MD5Util.MD5EncodeUtf8(passwordNew);
            int resultCount = userMapper.updatePasswordByUserId(password, userId);
            if (resultCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功!");
            }
            return ServerResponse.createByErrorMessage("修改密码失败!");
        }
        return ServerResponse.createByErrorMessage("验证码输入错误!");
    }

    @Override
    public ServerResponse<UserVo> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("获取当前用户信息失败！");
        }
        user.setPassword(StringUtils.EMPTY);
        UserVo userVo = assembleUserToUservo(user);
        return ServerResponse.createBySuccess(userVo);
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        //校验email
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已存在,请更换再次尝试更新!");
        }
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setUsername(user.getUsername());
        String md5Password = MD5Util.MD5EncodeUtf8(user.getPassword());
        newUser.setPassword(md5Password);
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setQuestion(user.getQuestion());
        newUser.setAnswer(user.getAnswer());
        newUser.setRole(user.getRole());
        ServerResponse validResponse = this.checkValid(newUser.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(newUser.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        resultCount = userMapper.updateByPrimaryKey(newUser);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功!", newUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败!");
    }

    @Override
    public ServerResponse<String> updatePhone(String phone, String rod, Integer userId) {
        if (this.determineIfTheCaptchaIsCorrect(rod) == 0) {
            //验证通过后的逻辑
            User user = new User();
            user.setId(userId);
            user.setPhone(phone);
            int resultCount = userMapper.updateByPrimaryKeySelective(user);
            if (resultCount > 0) {
                return ServerResponse.createBySuccessMessage("修改绑定手机成功!");
            }
            return ServerResponse.createByErrorMessage("修改绑定手机失败!");
        }
        return ServerResponse.createByErrorMessage("验证码输入错误!");
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    /**
     * 校验用户名或者邮箱是否重复
     *
     * @param str
     * @param type
     * @return
     */
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(str)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.selectUserByUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已经存在!");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.selectUserByEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已经存在!");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误!");
        }
        return ServerResponse.createBySuccessMessage("校验通过!");
    }

    /**
     * 转换日期成为str
     *
     * @param user
     * @return
     */
    public UserVo assembleUserToUservo(User user) {
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setUsername(user.getUsername());
        userVo.setPassword(user.getPassword());
        userVo.setEmail(user.getEmail());
        userVo.setPhone(user.getPhone());
        userVo.setQuestion(user.getQuestion());
        userVo.setRole(user.getRole());
        userVo.setCreateTime(DateTimeUtil.dateToStr(user.getCreateTime(), DateTimeUtil.STANDARD_FORMAT));
        userVo.setUpdateTime(DateTimeUtil.dateToStr(user.getUpdateTime(), DateTimeUtil.STANDARD_FORMAT));
        return userVo;
    }

    /**
     * 生成随机字符串
     *
     * @return
     */
    public String generateRandomString() {
        String result = "";
        for (int i = 0; i < 6; i++) {
            int intVal = (int) (Math.random() * 26 + 97);
            result = result + (char) intVal;
        }
        return result;
    }

    /**
     * 辅助类
     *
     * @param start
     * @param end
     * @return
     */
    public static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    /**
     * 生成随机Email
     *
     * @param lMin 最小长度
     * @param lMax 最大长度
     * @return
     */
    public static String getEmail(int lMin, int lMax) {
        final String[] email_suffix = "@yahoo.com,@msn.com,@hotmail.com,@aol.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn,@xtshop.com,@river.com".split(",");
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        int length = getNum(lMin, lMax);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = (int) (Math.random() * base.length());
            sb.append(base.charAt(number));
        }
        sb.append(email_suffix[(int) (Math.random() * email_suffix.length)]);
        return sb.toString();
    }

    /**
     * 判断验证码是否正确
     *
     * @param rod 验证码
     * @return 正确返回0 不正确返回1
     */
    public int determineIfTheCaptchaIsCorrect(String rod) {
        System.err.println(rod);
        System.err.println(PhoneCodeUtil.phoneCode);
        if (rod.equals(PhoneCodeUtil.phoneCode)) {
            return 0;
        }
        return 1;
    }
}
