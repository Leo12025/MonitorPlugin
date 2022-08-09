package com.leo12025.monitor;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.leo12025.monitor.Monitor.config;
import static com.leo12025.monitor.PushFeishu.sendFeishuMessage;

public class RAM implements Runnable {
    private final int ramPercent = config.getInt("Ram.ramPercent");

    @Override
    public void run() {
        //    RAM: "[%time%] 服务器内存不足! 服务器总内存: %max% | 已使用内存: %used% | 空闲内存: %free%"
        if (ramPercent <= 0 || ramPercent >= 100) return;

        long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
        long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
        long usedMemory = maxMemory - freeMemory;
        double percentUsed = usedMemory * 100.0D / maxMemory;
        if (ramPercent <= percentUsed) {

            // Log To Files Handling
            String message = "[%time%] 服务器内存不足! 服务器总内存: %max% | 已使用内存: %used% | 空闲内存: %free%";
            message = message.replace("%time%", DateTimeFormatter.ofPattern("yyyy MMM dd EE HH:mm", Locale.CHINA).format(ZonedDateTime.now())).replace("%max%", String.valueOf(maxMemory)).replace("%used%", String.valueOf(usedMemory)).replace("%free%", String.valueOf(freeMemory));
            sendFeishuMessage(message);


        }

    }

}
