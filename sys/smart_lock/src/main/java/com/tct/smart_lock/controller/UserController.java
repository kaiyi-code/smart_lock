package com.tct.smart_lock.controller;

import com.tct.smart_lock.domain.Result;
import com.tct.smart_lock.domain.User;
import com.tct.smart_lock.sevice.UserService;
import com.tct.smart_lock.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {


    @Autowired
    public UserService userService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/user/login")
    public Result login(@RequestBody Map<String,Object> map){
        try{
           String username = map.get("username").toString();
           String password = map.get("accesstoken").toString();
           User user = userService.FindNameAndPsw(username, password);
           if(user != null){
               logger.info("*****登陆成功*****");
               return ResultUtil.success(1,"login success");
           }else if(!userService.findByName(username).isEmpty()){
               logger.info("*****登陆失败（用户密码不正确）*****");
               return ResultUtil.failed(-1,"Password error");
           }else {
               logger.info("*****登陆失败（用户账号不正确）*****");
               return ResultUtil.failed(0,"No mobile phone number exists");
           }
        }catch (Exception e){
            logger.info("*****登陆存在异常*****");
            e.printStackTrace();
            return ResultUtil.failed(-2,"error caused by login");
        }

    }
    @PostMapping("/user/register")
    public Result register(@RequestBody Map<String,Object> map){
        try {
            String username = map.get("username").toString();
            String password = map.get("accesstoken").toString();
            if (userService.findByName(username).isEmpty()) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                userService.save(user);
                logger.info("*****注册成功*****");
                return ResultUtil.success(1,"register success");
            } else {
                logger.info("*****注册失败（该用户已经存在，不可被再次注册）*****");
                return ResultUtil.failed(0,"The phone number already exists and cannot be re-registered");
            }
        }catch (Exception e){
            logger.info("*****注册存在异常*****");
            e.printStackTrace();
            return ResultUtil.failed(-2,"error caused by register");
        }
    }

    @PostMapping("/user/changepassword")
    public Result changepassword(@RequestBody Map<String,Object> map){
        try {
            String username = map.get("username").toString();
            String password = map.get("accesstoken").toString();
            int number = userService.UpdatePassword(username, password);
            if (number == 1) {
                logger.info("*****更改密码成功*****");
                return ResultUtil.success(1,"changepassword success");
            } else {
                logger.info("*****更改密码失败（该用户账号不存在）*****");
                return ResultUtil.failed(0,"Not found phonenumber");
            }
        }catch(Exception e){
            logger.info("*****更改密码存在异常*****");
            e.printStackTrace();
            return ResultUtil.failed(-2,"error caused by changepassword");
        }
    }

}
