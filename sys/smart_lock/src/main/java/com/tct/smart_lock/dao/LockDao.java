package com.tct.smart_lock.dao;

import com.tct.smart_lock.domain.Locks;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LockDao {
    @Insert("insert into locks(lock_id, lock_state,reg_id,user_id,longitude,latitude,enable) " +
            "values (#{lockId}, #{lockState},#{regId}, #{userId},#{longitude}, #{latitude},#{enable})")
    int insert(Locks lock);

    @Select("select * from locks where user_id = #{user_id}")
    @Results({
            @Result(property = "lockId", column = "lock_id"),
            @Result(property = "lockState", column = "lock_state"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "regId",column = "reg_id")
    })
    List<Locks> findLockByUserId(String userId);

    @Select("select * from locks where lock_id = #{lock_id}")
    @Results({
            @Result(property = "lockId", column = "lock_id"),
            @Result(property = "lockState", column = "lock_state"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "regId",column = "reg_id")
    })
    Locks findLockByLockId(String lockId);

    @Update("update locks set reg_id = #{regId} where lock_id = #{lockId}")
    int setRegId(String lockId, String regId);

    @Update("update locks set longitude = #{longitude},latitude = #{latitude},address = #{address} where lock_id = #{lockId}")
    int setPosition(String lockId, double longitude, double latitude, String address);

    @Update("update locks set lock_state = #{state} where lock_id = #{lockId}")
    int updateLockState(String lockId, int state);

    @Update("update locks set enable = 1,user_id = #{username}where lock_id = #{lockId}")
    int setEnable(String lockId, String username);

    @Update("update locks set enable = 0 where lock_id = #{lockId}")
    int cleanEnable(String lockId);

    //重置所有的锁
    @Update("update locks set enable = 0 where enable = 1")
    int enableAll();

    @Delete("delete from locks where lock_id = #{lockId}")
    int deleteByLockId(String lockId);

    @Select("select * from locks")
    @Results({
            @Result(property = "lockId", column = "lock_id"),
            @Result(property = "lockState", column = "lock_state"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "regId",column = "reg_id")
    })
    List<Locks> all();
}
