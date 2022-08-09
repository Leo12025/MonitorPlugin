package com.leo12025.monitor;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.leo12025.monitor.Monitor.config;

public class HttpURLConnectionExample {

    private static final String USER_AGENT = "Mozilla/5.0";

    public static String getFeishuTokenRequest() throws Exception {

        String url = "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //添加请求头
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        json.put("app_id", config.getString("Feishu.app_id"));
        json.put("app_secret", config.getString("Feishu.app_secret"));

        String urlParameters = json.toString();

        //发送Post请求
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        //System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //打印结果
        //System.out.println(response);
        return response.toString();

    }

    public static String pushFeishuMessageRequest(String access_token, String groupId, String message) throws Exception {

        String url = "https://open.feishu.cn/open-apis/im/v1/messages?receive_id_type=chat_id";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //添加请求头
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        con.setRequestProperty("Authorization", "Bearer " + access_token);
        JSONObject json = new JSONObject();
        json.put("receive_id", groupId);
        JSONObject content = new JSONObject();
        content.put("text", message);
        json.put("content", content.toString());
        json.put("msg_type", "text");
        System.out.println(json);
        String urlParameters = json.toString();

        //发送Post请求
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(urlParameters.getBytes(StandardCharsets.UTF_8));
        //wr.writeChars(urlParameters);
        //wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        String responseMessage = con.getResponseMessage();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        //System.out.println("Response Code : " + responseCode);
        //System.out.println("Response Message : " + responseMessage);
        String inputLine;
        StringBuilder response = new StringBuilder();
        if(responseCode >=400 ){
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8));

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }else{
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }



        //打印结果
        //System.out.println(response);
        return response.toString();

    }
    public static String pushFeishuMessageWebHookRequest(String webHook,  String message) throws Exception {

        String url = webHook;
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //添加请求头
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        JSONObject content = new JSONObject();
        content.put("text", message);
        json.put("content", content.toString());
        json.put("msg_type", "text");
        System.out.println(json);
        String urlParameters = json.toString();

        //发送Post请求
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(urlParameters.getBytes(StandardCharsets.UTF_8));
        //wr.writeChars(urlParameters);
        //wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        String responseMessage = con.getResponseMessage();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        //System.out.println("Response Code : " + responseCode);
        //System.out.println("Response Message : " + responseMessage);
        String inputLine;
        StringBuilder response = new StringBuilder();
        if(responseCode >=400 ){
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8));

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }else{
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }



        //打印结果
        //System.out.println(response);
        return response.toString();

    }

    // HTTP GET请求
    private void sendGet() throws Exception {

        String url = "http://www.google.com/search?q=mkyong";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //默认值我GET
        con.setRequestMethod("GET");

        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //打印结果
        System.out.println(response);

    }

    // HTTP POST请求
    private void sendPost() throws Exception {

        String url = "https://selfsolve.apple.com/wcResults.do";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //添加请求头
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "";

        //发送Post请求
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //打印结果
        System.out.println(response);

    }

}