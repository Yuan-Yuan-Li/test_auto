package com.filez.zbox.console.testcase;


import com.alibaba.fastjson.*;
import com.filez.zbox.console.Asserts.AssertBase;
import com.filez.zbox.console.Properties.PropertyConfig;
import org.apache.commons.io.IOUtils;
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

    public static String cookies;
    public static Integer uid;
    public static String host = PropertyConfig.getData("hosts");
    public static String username = PropertyConfig.getData("username");
    public static String password = PropertyConfig.getData("password");
    public static String loginType = PropertyConfig.getData("loginType");


    @Test(description = "登陆",groups = {"smoke","login"},enabled = true)
    public static  void test_log() throws IOException {
        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
        String loginURL ="https://"+host+"/v2/user/login_new?";
        HttpClient httpClient = null;
        httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(loginURL);
        List<NameValuePair> request_params = new ArrayList<NameValuePair>();
        request_params.add(new BasicNameValuePair("loginkey", username));
        request_params.add(new BasicNameValuePair("password", password));
        request_params.add(new BasicNameValuePair("loginType", loginType));
        request.setEntity(new UrlEncodedFormEntity(request_params, "UTF-8"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(request_params, "UTF-8");
        request.setEntity(urlEncodedFormEntity);
        HttpResponse response = httpClient.execute(request);
        String string = IOUtils.toString(response.getEntity().getContent());
        JSONObject object = JSONObject.parseObject(string);
        cookies = object.get("X-LENOVO-SESS-ID").toString();
        uid = Integer.parseInt(object.get("uid").toString());
        AssertBase.assertCode(response,200);
        logger.debug("cookie：" + cookies);
    }

    @Test(description = "退出",groups = {"smoke","logout"},enabled = true)
    public static  void test_logout() throws IOException {
        test_log();
        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
        String loginURL ="https://"+host+"/v2/user/logout?";
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(loginURL);
        List<NameValuePair> request_params = new ArrayList<NameValuePair>();
        request_params.add(new BasicNameValuePair("user_slug", "email:"+username));
        request.setHeader("Cookie", "X-LENOVO-SESS-ID=" + cookies);
        request.setEntity(new UrlEncodedFormEntity(request_params, "UTF-8"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(request_params, "UTF-8");
        request.setEntity(urlEncodedFormEntity);
        HttpResponse response = httpClient.execute(request);
        String string = IOUtils.toString(response.getEntity().getContent());
        JSONObject object = JSONObject.parseObject(string);
        AssertBase.assertCode(response,200);
    }

    //重置密码
    //找回密码



//
//    @Test(description = "二次验证",groups = {"smoke","IP"},enabled = true)
//    public static void second_auth ()throws IOException {
//        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
//        String loginURL ="https://"+host+"/v2/account/second_auth/set";
//        HttpPost request = new HttpPost(loginURL);
//        test_log();
//        HttpClient httpClient = new DefaultHttpClient();
//        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,600000);
//        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,600000);
//        httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
//        request.setHeader("Cookie", "X-LENOVO-SESS-ID=" + cookies);
//        request.setHeader(new BasicHeader("Accept" ,"application/x-www-form-urlencoded"));
//        List<NameValuePair> request_params = new ArrayList<NameValuePair>();
//        request_params.add(new BasicNameValuePair("account_id", "1"));
//        request_params.add(new BasicNameValuePair("uid", "1"));
//        request_params.add(new BasicNameValuePair("second_auth", "1"));
//        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(request_params, "UTF-8");
//        request.setEntity(urlEncodedFormEntity);
//        HttpResponse response = httpClient.execute(request);
//        String string = IOUtils.toString(response.getEntity().getContent());
//        //logger.debug(string);
//        AssertBase.assertCode(response,200);
//        logger.debug(response);
//    }
//
//
//
//
//    @Test(description = "IP管控",groups = {"smoke","IP"},enabled = true)
//    public static void security()throws IOException {
//        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
//        String loginURL ="https://"+host+"/v2/account/security/restrict/set";
//        HttpPost request = new HttpPost(loginURL);
//        test_log();
//        HttpClient httpClient = new DefaultHttpClient();
//        request.setHeader("Cookie", "X-LENOVO-SESS-ID=" + cookies);
//        List<NameValuePair> request_params = new ArrayList<NameValuePair>();
//        request_params.add(new BasicNameValuePair("account_id", "1"));
//        request_params.add(new BasicNameValuePair("uid", "1"));
//        request_params.add(new BasicNameValuePair("category", "device"));
//        request.setEntity(new UrlEncodedFormEntity(request_params, "UTF-8"));
//        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(request_params, "UTF-8");
//        request.setEntity(urlEncodedFormEntity);
//        HttpResponse response = httpClient.execute(request);
//        String string = IOUtils.toString(response.getEntity().getContent());
//        //logger.debug(string);
//        AssertBase.assertCode(response,200);
//        logger.debug(response);
//    }
//
//    @Test(description = "IP管控",groups = {"smoke","IP"},enabled = true)
//    public static void secur()throws IOException {
//        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
//        String loginURL ="https://"+host+"/v2/config/set?";
//        HttpPost request = new HttpPost(loginURL);
//        test_log();
//        HttpClient httpClient = new DefaultHttpClient();
//        request.setHeader("Cookie", "X-LENOVO-SESS-ID=" + cookies);
//        List<NameValuePair> request_params = new ArrayList<NameValuePair>();
//        request_params.add(new BasicNameValuePair("account_id", "1297379"));
//        request_params.add(new BasicNameValuePair("uid", "1204235"));
//        request_params.add(new BasicNameValuePair("json", "[{\"config_type\":1,\"config_id\":1297379,\"name\":\"team.quota.default\",\"value\":10737418240},{\"config_type\":1,\"config_id\":1297379,\"name\":\"team.member.default\",\"value\":\"5\"},{\"config_type\":1,\"config_id\":1297379,\"name\":\"team.grantauth.default\",\"value\":\"2009\"},{\"config_type\":1,\"config_id\":1297379,\"name\":\"team.dc.default\",\"value\":\"-1\"}]"));
//        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(request_params, "UTF-8");
//        request.setEntity(urlEncodedFormEntity);
//        HttpResponse response = httpClient.execute(request);
//        String string = IOUtils.toString(response.getEntity().getContent());
//        logger.debug(string);
//        AssertBase.assertCode(response,200);
//        logger.debug(response);
//     }

}
