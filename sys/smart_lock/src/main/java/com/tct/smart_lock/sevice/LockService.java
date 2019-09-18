package com.tct.smart_lock.sevice;


import com.tct.smart_lock.domain.Locks;

import java.util.List;

public interface LockService {
    List<Locks> all();
    int register(Locks lock);
    List<Locks> findLockByUserId(String userId);
    Locks findLockByLockId(String lockId);
    int setRegId(String lockId, String regId);
    int setPosition(String lockId, double longitude, double latitude, String address);
    int setEnable(String lockId, String username);
    int cleanEnable(String lockId, String username);
    int enableAll();
    int sendState(String lockId, int state);
    int setLockState(String lockId, String userId);
    int cleanLockState(String lockId, String userId);
    int deleteByLockId(String lockId);
}
