package com.filez.zbox.console;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Log {
    public static final Logger logger = Logger.getLogger(Log.class);

    private static HttpClient httpClient = new DefaultHttpClient();
    private static HttpPost httppost;
    private static HttpResponse response;
    private static HttpEntity entity;
    private static String postResult = null;



    @Test(description = "登陆",groups = {"smoke","a"},enabled = true)
    public static  void log_test() throws IOException {
        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
        String loginURL ="https://qa-t3.vips100.com/user/login";
        httppost = new HttpPost(loginURL);
        List<NameValuePair> request_params = new ArrayList<NameValuePair>();
        request_params.add(new BasicNameValuePair("loginkey", "729090880@qq.com"));
        request_params.add(new BasicNameValuePair("password", "123qwe"));
        request_params.add(new BasicNameValuePair("loginType", "0"));
        httppost.setEntity(new UrlEncodedFormEntity(request_params, "UTF-8"));
        response = httpClient.execute(httppost);
        /*entity = response.getEntity();
        // 在这里可以用Jsoup之类的工具对返回结果进行分析，以判断创建是否成功
        postResult = EntityUtils.toString(entity, "UTF-8");*/
        //JSONObject jsonObject =JSONObject.parseObject(postResult);
        logger.debug("response："+response);

    }
    @Test(description = "不猜",groups = {"smoke","b"},enabled = true)
    public static void m(){
        logger.debug("this is m debug test");
    }
}
