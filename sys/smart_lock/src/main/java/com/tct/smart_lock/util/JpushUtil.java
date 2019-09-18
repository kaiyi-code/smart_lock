package com.tct.smart_lock.util;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JpushUtil {
    private static Logger logger = LoggerFactory.getLogger(JpushUtil.class);
    private static String appKey = "d2961178b43e4271c311562f";
    private static String masterSecret = "63b8268e6736fbd8fbe37473";
    public static JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());

    public static int jpushAndroid(String regId, String msg){
        PushPayload payload = PushPayload.newBuilder()
                .setAudience(Audience.registrationId(regId))
                .setPlatform(Platform.android())
                .setMessage(Message.content(msg))
                .setNotification(Notification.android(msg,"smart_lock",null)).build();

        try {
            PushResult result = jpushClient.sendPush(payload);
            logger.info("result: "+ result);
            return 1;
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            e.printStackTrace();
            return -1;

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            e.printStackTrace();
            return -2;
        }
    }
}
