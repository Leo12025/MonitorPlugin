package com.leo12025.monitor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.leo12025.monitor.Monitor.dataFolder;

public class PlayerData {

    public static String readPlayerDataForFile(String playerUUID) {
        //TODO: 用文件流的形式读取出一个指定玩家的JsonString...
        File jsonFile = new File(dataFolder.getPath() + "\\playerData\\" + playerUUID + ".json");
        try {

            FileReader fileReader = new FileReader(jsonFile);
            BufferedReader reader = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            while (true) {

                int ch = reader.read();
                if (ch != -1) {

                    sb.append((char) ch);
                } else {

                    break;
                }
            }
            fileReader.close();
            reader.close();
            return sb.toString();
        } catch (IOException e) {

            return "";
        }

    }


    public static Boolean savePlayerDataForFile(String playerUUID, String playerData) throws IOException {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(Files.newOutputStream(Paths.get(dataFolder.getPath() + "\\playerData\\" + playerUUID + ".json")), StandardCharsets.UTF_8);
            osw.write(playerData);
            osw.flush();//清空缓冲区，强制输出数据
            osw.close();//关闭输出流
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static JSONObject getPlayerData(String playUUID) {
        String fileObject = readPlayerDataForFile(playUUID);
        JSONObject obj;
        //TODO: 如果文件不存在或者为空，那么输出一个空的Json对象，否则读取文件
        if (fileObject.equals("")) {
            obj=new JSONObject();
        } else {
            obj= new JSONObject(fileObject);
        }

        /*
        *
        System.out.println("FLAG:"+obj.getString("FLAG"));
        System.out.println("NAME:"+obj.getString("NAME"));

        //获取数组
        JSONArray arr = obj.getJSONArray("ARRAYS");
        System.out.println("数组长度:"+arr.length());
        for(int i=0;i<arr.length();i++)
        {
            JSONObject subObj = arr.getJSONObject(i);
            System.out.println("数组Name:"+subObj.getString("Name")+" String:"+subObj.getString("String"));
        }
        *
        * */


        return obj;
    }


}
