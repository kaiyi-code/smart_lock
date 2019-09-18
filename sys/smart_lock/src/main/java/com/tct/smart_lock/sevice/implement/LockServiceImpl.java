package com.tct.smart_lock.sevice.implement;

import com.tct.smart_lock.dao.LockDao;
import com.tct.smart_lock.domain.Locks;
import com.tct.smart_lock.sevice.LockService;
import com.tct.smart_lock.util.JpushUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("LockService")
public class LockServiceImpl implements LockService {

    @Resource
    LockDao lockDao;


    @Override
    public List<Locks> all() {
        return lockDao.all();
    }

    @Override
    public List<Locks> testall() {
        return lockDao.testall();
    }

    @Override
    public int register(Locks lock) {
        return lockDao.insert(lock);
    }
    @Override
    public List<Locks> findLockByUserId(String userId) {
        return lockDao.findLockByUserId(userId);
    }
    @Override
    public Locks findLockByLockId(String lockId) {
        return lockDao.findLockByLockId(lockId);
    }

    @Override
    public int setRegId(String lockId,String regId) {
        return lockDao.setRegId(lockId,regId);
    }

    @Override
    public int setPosition(String lockId, double longitude, double latitude,String address) {
        return lockDao.setPosition(lockId,longitude,latitude,address);
    }

    @Override
    public int setEnable(String lockId, String username) {
        Locks lock = lockDao.findLockByLockId(lockId);
        if (lock.getEnable() == 0){
            int result = JpushUtil.jpushAndroid(lock.getRegId(),"setEnable:"+lockId);
            if (result == 1){
                return lockDao.setEnable(lockId,username);
            }else {
                lockDao.setEnable(lockId,username);
                return -2;
            }
        }
        else {
            return 0;
        }
    }

    @Override
    public int cleanEnable(String lockId,String username) {
        Locks lock = lockDao.findLockByLockId(lockId);
        if (!lock.getUserId().equals(username)){
            return -1;
        } else if (lock.getEnable() == 1){
                int result = JpushUtil.jpushAndroid(lock.getRegId(),"cleanEnable:"+lockId);
                if (result == 1){
                    return lockDao.cleanEnable(lockId);
                }else {
                    lockDao.cleanEnable(lockId);
                    return -2;
                }
        }else {
            return 0;
        }
    }

    @Override
    public int enableAll() {
        return lockDao.enableAll();
    }

    @Override
    public int sendState(String lockId, int state) {
        Locks lock = lockDao.findLockByLockId(lockId);
        if (lock.getLockState() == state){
            return 0;
        }
        else {
            return lockDao.updateLockState(lockId, state);
        }
    }

    @Override
    public int setLockState(String lockId,String userId) {
        Locks lock = lockDao.findLockByLockId(lockId);
        if (lock.getEnable() == 0){
            return 3;
        }
        else if (lock.getLockState() == 1){
            return 0;
        }
        else if (!lock.getUserId().equals(userId)){
            return -4;
        }
        else if (lock.getRegId().equals("000000" )|| lock.getRegId().isEmpty()){
            return -3;
        }
        else {
            int result = JpushUtil.jpushAndroid(lock.getRegId(), "open:"+lockId);
            if (result == 1) {
                lockDao.updateLockState(lockId, 1);
            }
            return result;
        }
    }

    @Override
    public int cleanLockState(String lockId,String userId) {
        Locks lock = lockDao.findLockByLockId(lockId);
        if (lock.getEnable() == 0){
            return 3;
        }
        else if (lock.getLockState() == 0){
            return 0;
        }else if (!lock.getUserId().equals(userId)){
            return -4;
        }
        else if (lock.getRegId().equals("000000" )|| lock.getRegId().isEmpty()){
            return -3;
        }
        else {
            int result = JpushUtil.jpushAndroid(lock.getRegId(), "close:"+lockId);
            if (result == 1) {
                lockDao.updateLockState(lockId, 0);
            }
            return result;
        }
    }

    @Override
    public int deleteByLockId(String lockId) {
        return lockDao.deleteByLockId(lockId);
    }
}
