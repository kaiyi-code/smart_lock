package com.tct.smart_lock.controller;

import com.tct.smart_lock.domain.Locks;
import com.tct.smart_lock.domain.Result;
import com.tct.smart_lock.sevice.LockService;
import com.tct.smart_lock.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lock")
public class LockController {

    @Resource
    LockService lockService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    //查看所有的车位锁信息
    @RequestMapping("/all")
    public Result all(){
        List<Locks>result =  lockService.all();
        logger.info("return info of all valid locks");
        return ResultUtil.success(1,result);
    }

    //查看所有的车位锁信息
    @RequestMapping("/test_all")
    public Result testAll(){
        List<Locks>result =  lockService.testall();
        logger.info("return info of all locks");
        return ResultUtil.success(1,result);
    }


    //注册新的车位锁
    @RequestMapping("/register")
    public Result register(@RequestBody Map<String,Object> map){
        Locks lock = new Locks();
        lock.setLockId(map.get("lock_id").toString());
        try {
            lock.setUserId(map.get("user_id").toString());
        }catch (Exception e){
            logger.info("register lock with default user_id:1234567");
            lock.setUserId("1234567");//默认为user_id为1234567
        }

        try {
            lock.setLockState(Integer.parseInt(map.get("lock_state").toString()));
        }catch (Exception e){
            logger.info("register lock with default lock_state: 1");
            lock.setLockState(0);//默认设为0,灯是灭的
        }
        try {
            lock.setRegId(map.get("reg_id").toString());
        }catch (Exception e){
            logger.info("register lock with default reg_id: 000000. You should set correct regId before changeLockState");
            lock.setRegId("000000");
        }
        try {
            lock.setLongitude(Double.parseDouble(map.get("longitude").toString()));
            lock.setLatitude(Double.parseDouble(map.get("latitude").toString()));
        }catch (Exception e){
            logger.info("register lock with default longitude and latitude: (-1,-1)");
            lock.setLongitude(-1);
            lock.setLatitude(-1);
        }
        try{
            lock.setAddress(map.get("address").toString());
        }catch (Exception e){
            logger.info("register lock with default address: null");
            lock.setAddress(null);
        }
        logger.info("register lock with default enable: 0");
        lock.setEnable(0);//0表示车位可用，1表示车位不可用

        int result = lockService.register(lock);
        if (result == 1){
            logger.info("register lock:"+lock.getLockId() + " successfully");
        }
        else {
            logger.info("failed register lock:"+lock.getLockId());
        }
        return ResultUtil.success(result,lock);
    }

    //通过userId查看相应的车位锁的信息，可能一个用户有多个车位，及一个用户绑定多个车位锁
    @RequestMapping("/findLockByUserId")
    public Result findLockByUserId(@RequestBody Map<String,Object>map){
        String userId = map.get("user_id").toString();
        List<Locks> result =  lockService.findLockByUserId(userId);
//        System.out.println(result);
        if (result != null){
            logger.info("findLockByUserId success");
            return ResultUtil.success(1,result);
        }
        else {
            logger.info("findLockByUserId failed");
            return ResultUtil.failed(0,"Not found user "+userId);
        }
    }

    //通过lockId查看相应的车位锁信息
    @RequestMapping("/findLockByLockId")
    public Result findLockByLockId(@RequestBody Map<String,Object>map){
        String lockId = map.get("lock_id").toString();
        Locks result = lockService.findLockByLockId(lockId);
        System.out.println(result);
        if (result != null){
            logger.info("findLockByLockId success");
            return ResultUtil.success(1,result);
        }
        else {
            logger.info("findLockByLockId failed");
            return ResultUtil.failed(0,"Not Found lock "+lockId);
        }
    }

    //设置注册id，用于极光推送时使用
    @RequestMapping("/setRegId")
    public Result setRegId(@RequestBody Map<String,Object>map){
        String regId = map.get("reg_id").toString();
        String lockId = map.get("lock_id").toString();
        int result = lockService.setRegId(lockId,regId);
        logger.info("setRegId success");
        return ResultUtil.success(result,map);
    }

    //设置位置信息
    @RequestMapping("/setPosition")
    public Result setPosition(@RequestBody Map<String,Object>map){
        String lockId = map.get("lock_id").toString();
        double longitude = Double.parseDouble(map.get("longitude").toString());
        double latitude = Double.parseDouble(map.get("latitude").toString());
        String address = map.get("address").toString();
        logger.info(address);
        int result = lockService.setPosition(lockId,longitude,latitude,address);
        logger.info("setPosition success");
        return ResultUtil.success(result,map);
    }

    //设置enable状态，包括set和clean，用于判断车位锁是否被占用
    @RequestMapping("/setEnable")
    public Result setEnable(@RequestBody Map<String,Object>map){
        String lockId = map.get("lock_id").toString();
        String username = map.get("user_id").toString();
        int result = lockService.setEnable(lockId,username);
        if (result == 1){
            logger.info("setEnable success");
            return ResultUtil.success(result,lockService.findLockByLockId(lockId));
        }else if (result == -2){
            logger.info("jPush failed");
            return ResultUtil.success(result,lockService.findLockByLockId(lockId));
        }
        else {
            logger.info("setEnable failed");
            return ResultUtil.failed(result,"failed or don't need to set Enable");
        }
    }
    @RequestMapping("/cleanEnable")
    public Result cleanEnable(@RequestBody Map<String,Object>map){
        String lockId = map.get("lock_id").toString();
        String username = map.get("user_id").toString();

        int result = lockService.cleanEnable(lockId,username);
        if (result == 1) {
            logger.info("cleanEnable success");
            return ResultUtil.success(result, lockService.findLockByLockId(lockId));
        }else if (result == -1){
            logger.info("clean failed: username mismatch.you should enter correct username");
            return ResultUtil.failed(result,"username mismatch.you should enter correct username");
        }else if (result == -2){
            logger.info("jPush failed");
            return ResultUtil.success(result,lockService.findLockByLockId(lockId));
        }
        else {
            logger.info("cleanEnable failed");
            return ResultUtil.failed(result,"failed or don't need to clean Enable");
        }
    }

    //重置所有的车位锁的占用状态，超级用户使用，一般不使用
    @RequestMapping("/enableAll")
    public Result enableAll(){
        int result = lockService.enableAll();
        logger.info("enable all of the locks successfully。Result=: "+result);
        return ResultUtil.success(result,"enable all of the locks successfully");
    }
    @RequestMapping("/sendState")
    public Result sendState(@RequestBody Map<String,Object>map){
        String lockId = map.get("lock_id").toString();
        int state = Integer.parseInt(map.get("lock_state").toString());
        int result = lockService.sendState(lockId,state);
        if (result == 1){
            logger.info("send lock_state success");
            return ResultUtil.success(result,lockService.findLockByLockId(lockId));
        }
        else if (result == 0){
            logger.info("send lock_state success: lockState do not need to change");
            return ResultUtil.success(result,"lockState do not need to change");
        }
        else {
            logger.info("send lock_state failed: sendState() failed");
            return ResultUtil.failed(result,"sendState() failed");
        }
    }
    //控制车位锁的状态，会先判断enable状态是否被占用
    @RequestMapping("/changeLockState")
    public Result changeLockState(@RequestBody Map<String,Object>map){
        String lockId = map.get("lock_id").toString();
        String userId = map.get("user_id").toString();
        int state = Integer.parseInt(map.get("lock_state").toString());
        int result = -1;
        if (state == 0){
            result = lockService.cleanLockState(lockId,userId);
        }else if (state == 1){
            result = lockService.setLockState(lockId,userId);
        }

        if (result == 1){
            logger.info("change lock_state success");
            return ResultUtil.success(result,lockService.findLockByLockId(lockId));
        }
        else if (result == -4){
            logger.info("failed: user_id mismatch");
            return ResultUtil.failed(result,"failed: user_id mismatch");
        }
        else if (result == 3){
            logger.info("failed: the lock: " + lockId + " should become disabled firstly");
            return ResultUtil.failed(result,"the lock: " + lockId + " should become disabled firstly");
        }
        else if (result == -3){
            logger.info("change lock_state failed: please setRegId firstly");
            return ResultUtil.failed(result,"please setRegId firstly");
        }
        else if (result == 0){
            logger.info("change lock_state failed: lockState do not need to change");
            return ResultUtil.failed(result,"lockState do not need to change");
        }
        else {
            logger.info("change lock_state failed: changeLockState() failed");
            return ResultUtil.failed(result,"changeLockState() failed");
        }
    }

    //注销lock，超级用户，一般不使用
    @RequestMapping("/deleteByLockId")
    public Result deleteByLockId(@RequestBody Map<String,Object>map){
        String lockId = map.get("lock_id").toString();
        int result =  lockService.deleteByLockId(lockId);
        logger.info("Has deleted lock:"+lockId);
        return ResultUtil.success(result,"Has deleted lock:"+lockId);
    }
}
