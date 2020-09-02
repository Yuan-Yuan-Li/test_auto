package com.filez.zbox.console.testcase;


import com.alibaba.fastjson.JSON;
import com.filez.zbox.console.Asserts.AssertBase;
import com.filez.zbox.console.Properties.PropertyConfig;
import com.typesafe.config.Config;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.typesafe.config.ConfigFactory;

public class Log {
    public static final Logger logger = Logger.getLogger(Log.class);

    public static String cookies;

    public static String host = null;

    public static int account_id = 0;

    public static int var_uid = 0;

    static {
        Config conf = ConfigFactory.parseFile(new File("src/resource/conf/env.properties"));
        String s = conf.toString();

        host =  PropertyConfig.getData("hosts");
    }



    @Test(description = "登陆",groups = {"smoke","a"},enabled = true)
    public static  void log() throws IOException {
        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
        String host = PropertyConfig.getData("test.hosts");
        String loginURL ="https://boxqa.lenovo.com/v2/user/login_new?";
        //String loginURL ="https://"+host+"/v2/user/login_new?";
        HttpClient httpClient = null;
        httpClient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(loginURL);
        List<NameValuePair> request_params = new ArrayList<NameValuePair>();
        request_params.add(new BasicNameValuePair("loginkey", "zhangt2@test.com"));
        request_params.add(new BasicNameValuePair("password", "123qwe"));
        request_params.add(new BasicNameValuePair("loginType", "0"));
        httppost.setEntity(new UrlEncodedFormEntity(request_params, "UTF-8"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(request_params, "UTF-8");
        httppost.setEntity(urlEncodedFormEntity);
        HttpResponse response = httpClient.execute(httppost);
        String string = IOUtils.toString(response.getEntity().getContent());
        JSONObject obj = (JSONObject) JSON.parse(string);
         obj.get("uid");
        Header[] headers = response.getHeaders("Set-Cookie");
        Assert.assertNotNull(headers);
        for (Header header : headers) {
            String s = header.getValue();
            if (s.contains("X-LENOVO-SESS-ID=")) {
                HeaderElement[] elements = header.getElements();
                for (HeaderElement element : elements) {
                    if (element.getName().equals("X-LENOVO-SESS-ID")) {
                        cookies = element.getValue();
                    }
                }
                break;
            }
        }
        logger.debug("cookie：" + response);
    }


    @Test(description = "二次验证",groups = {"smoke","IP"},enabled = true)
    public static void second_auth ()throws IOException {
        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
        String loginURL ="https://qa-t3.vips100.com/v2/account/second_auth/set";
        HttpPost request = new HttpPost(loginURL);
        log();
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,600000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,600000);
        httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
        request.setHeader("Cookie", "X-LENOVO-SESS-ID=" + cookies);
        request.setHeader(new BasicHeader("Accept" ,"application/x-www-form-urlencoded"));
        List<NameValuePair> request_params = new ArrayList<NameValuePair>();
        request_params.add(new BasicNameValuePair("account_id", "1"));
        request_params.add(new BasicNameValuePair("uid", "1"));
        request_params.add(new BasicNameValuePair("second_auth", "1"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(request_params, "UTF-8");
        request.setEntity(urlEncodedFormEntity);
        HttpResponse response = httpClient.execute(request);
        String string = IOUtils.toString(response.getEntity().getContent());
        //logger.debug(string);
        AssertBase.assertCode(response);
        logger.debug(response);
    }




    @Test(description = "IP管控",groups = {"smoke","IP"},enabled = true)
    public static void security()throws IOException {
        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
        String loginURL ="https://qa-t3.vips100.com/v2/account/security/restrict/set";
        HttpPost request = new HttpPost(loginURL);
        log();
        HttpClient httpClient = new DefaultHttpClient();
        request.setHeader("Cookie", "X-LENOVO-SESS-ID=" + cookies);
        List<NameValuePair> request_params = new ArrayList<NameValuePair>();
        request_params.add(new BasicNameValuePair("account_id", "1"));
        request_params.add(new BasicNameValuePair("uid", "1"));
        request_params.add(new BasicNameValuePair("category", "device"));
        request.setEntity(new UrlEncodedFormEntity(request_params, "UTF-8"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(request_params, "UTF-8");
        request.setEntity(urlEncodedFormEntity);
        HttpResponse response = httpClient.execute(request);
        String string = IOUtils.toString(response.getEntity().getContent());
        //logger.debug(string);
        AssertBase.assertCode(response);
        logger.debug(response);
    }

    @Test(description = "IP管控",groups = {"smoke","IP"},enabled = true)
    public static void secur()throws IOException {
        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
        String loginURL ="https://console.boxqa.lenovo.com/v2/config/set?";
        HttpPost request = new HttpPost(loginURL);
        log();
        HttpClient httpClient = new DefaultHttpClient();
        request.setHeader("Cookie", "X-LENOVO-SESS-ID=" + cookies);
        List<NameValuePair> request_params = new ArrayList<NameValuePair>();
        request_params.add(new BasicNameValuePair("account_id", "1297379"));
        request_params.add(new BasicNameValuePair("uid", "1204235"));
        request_params.add(new BasicNameValuePair("json", "[{\"config_type\":1,\"config_id\":1297379,\"name\":\"team.quota.default\",\"value\":10737418240},{\"config_type\":1,\"config_id\":1297379,\"name\":\"team.member.default\",\"value\":\"5\"},{\"config_type\":1,\"config_id\":1297379,\"name\":\"team.grantauth.default\",\"value\":\"2009\"},{\"config_type\":1,\"config_id\":1297379,\"name\":\"team.dc.default\",\"value\":\"-1\"}]"));
        //request.setEntity(new UrlEncodedFormEntity(request_params, "UTF-8"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(request_params, "UTF-8");
        request.setEntity(urlEncodedFormEntity);
        HttpResponse response = httpClient.execute(request);
        String string = IOUtils.toString(response.getEntity().getContent());
        logger.debug(string);
        AssertBase.assertCode(response);
        logger.debug(response);
    }
}
