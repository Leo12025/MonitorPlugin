package com.leo12025.monitor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.leo12025.monitor.Monitor.config;
import static com.leo12025.monitor.PushFeishu.sendFeishuMessage;


public class TPS implements Runnable {
    private int tickCount = 0;
    private final long[] ticks = new long[600];
    private int tpsMedium =config.getInt("Tps.tpsMedium");
    private int tpsCritical= config.getInt("Tps.tpsCritical");

    @Override
    public void run() {
        if (this.getTPS() <= 1 ) return;
        if (tpsMedium <= 0 || tpsMedium >= 20 || tpsCritical <= 0 || tpsCritical >= 20) return;

        this.ticks[(this.tickCount % this.ticks.length)] = System.currentTimeMillis();

        this.tickCount += 1;

        if (this.getTPS() <= tpsMedium) {
            String message="[%time%] 服务器TPS已经降到 %TPS%";
            message=message.replace("%time%", DateTimeFormatter.ofPattern("yyyy MMM dd EE HH:mm", Locale.CHINA).format(ZonedDateTime.now())).replace("%TPS%", String.valueOf(getTPS()));
            sendFeishuMessage(message);
            //BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getTPSLogFile(), true));
            //out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Server-Side.TPS-Medium")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%TPS%", String.valueOf(getTPS())) + "\n");
            //out.close();

        } else if (this.getTPS() <= tpsCritical) {
            String message="[%time%] 警告！服务器TPS已经骤降至 %TPS%";

            message=message.replace("%time%", DateTimeFormatter.ofPattern("yyyy MMM dd EE HH:mm", Locale.CHINA).format(ZonedDateTime.now())).replace("%TPS%", String.valueOf(getTPS()));
            sendFeishuMessage(message);

            //BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getTPSLogFile(), true));
            //out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Server-Side.TPS-Critical")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%TPS%", String.valueOf(getTPS())) + "\n");
            //out.close();

        }

    }


    private double getTPS() {
        return getTPS(100);
    }

    private double getTPS(int ticks) {

        if (this.tickCount <= ticks) return 20.0D;
        int target = (this.tickCount - 1 - ticks) % this.ticks.length;
        long elapsed = System.currentTimeMillis() - this.ticks[target];
        return ticks / (elapsed / 1000.0D);

    }

}
