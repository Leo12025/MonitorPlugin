package com.leo12025.monitor;

import org.json.JSONObject;

import java.util.Objects;
import java.util.logging.Level;

import static com.leo12025.monitor.HttpURLConnectionExample.*;
import static com.leo12025.monitor.Monitor.config;
import static com.leo12025.monitor.Monitor.logger;

public class PushFeishu {
    static String FeishuToken = "";
    static Long FeishuTokenTimestamp = 0L;

    public static String getFeishuToken() {
        long TimeNow = System.currentTimeMillis();
        System.out.println(Objects.equals(FeishuToken, ""));
        if (Objects.equals(FeishuToken, "")) {
            FeishuToken = config.getString("Feishu.Token");
            FeishuTokenTimestamp = config.getLong("Feishu.TokenTimestamp");
            logger.log(Level.INFO, "[feishuToken]尝试从配置文件中加载令牌 " + FeishuToken + "，" + FeishuTokenTimestamp + " ！"); //这里肯定是空的，执行正确

            System.out.println(Objects.equals(FeishuToken, ""));

            if (Objects.equals(FeishuToken, "") || FeishuToken == null) {
                //直接获取
                // FeishuToken = "T" + TimeNow;
                //FeishuTokenTimestamp = TimeNow + 30;}

                try {
                    JSONObject req = new JSONObject(getFeishuTokenRequest());//getFeishuTokenRequest()
                    if (req.getInt("code") == 0) {
                        FeishuToken = req.getString("tenant_access_token");
                        FeishuTokenTimestamp = TimeNow + (req.getInt("expire") * 1000L);
                        config.set("Feishu.Token", FeishuToken);
                        config.set("Feishu.TokenTimestamp", FeishuTokenTimestamp);
                        logger.log(Level.INFO, "[feishuToken]检测到空令牌，正在获取新令牌 " + FeishuToken + "，" + FeishuTokenTimestamp + " ！");
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {

            }


        }

        System.out.println("Now: " + TimeNow + "  FeishuTokenTimestamp: " + FeishuTokenTimestamp);
        if (TimeNow - FeishuTokenTimestamp > 0) {
            // 假设 现在的时间 - 到期时间 > 0 ，则 说明到期，需要重新获取刷新
            //FeishuToken = "T" + TimeNow;
            //FeishuTokenTimestamp = TimeNow + 30;
            try {
                JSONObject req = new JSONObject(getFeishuTokenRequest());//getFeishuTokenRequest()
                if (req.getInt("code") == 0) {
                    FeishuToken = req.getString("tenant_access_token");
                    FeishuTokenTimestamp = TimeNow + (req.getInt("expire") * 1000L);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            config.set("Feishu.Token", FeishuToken);
            config.set("Feishu.TokenTimestamp", FeishuTokenTimestamp);

            logger.log(Level.INFO, "[feishuToken]令牌即将过期或者已过期，正在申请新令牌 " + FeishuToken + "，" + FeishuTokenTimestamp + " ！");

        } else {
            logger.log(Level.INFO, "[feishuToken]使用了令牌 " + FeishuToken + "，" + FeishuTokenTimestamp + " ！");

        }
        return FeishuToken;

    }

    public static void sendFeishuMessage(String message)  {
        if(!Objects.equals(config.getString("Feishu.webHook"), "")){
        //TODO
            try {
                pushFeishuMessageWebHookRequest(config.getString("Feishu.webHook"),"[" + config.getString("Monitor.serverName") + "]" +message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{

            String FeishuTokenUse = getFeishuToken();
            try {
                pushFeishuMessageRequest(FeishuTokenUse, config.getString("Feishu.sendGroup"), "[" + config.getString("Monitor.serverName") + "]" + message);
            } catch (Exception e) {
                //throw new RuntimeException(e);
                e.printStackTrace();
            }
        }
        logger.log(Level.INFO, "[FeishuPush]向飞书推送了信息 " + message + " ！");


    }
}
