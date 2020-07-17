package testcase;


import com.filez.zbox.console.Asserts.AssertBase;
import org.apache.http.*;
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

import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Log {
    public static final Logger logger = Logger.getLogger(Log.class);

    private static HttpResponse response;
    public static String cookies;


    @Test(description = "登陆",groups = {"smoke","a"},enabled = true)
    public static  void log() throws IOException {
        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
        String loginURL ="https://qa-t3.vips100.com/v2/user/login_new?";
        HttpClient httpClient = null;
        httpClient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(loginURL);
        List<NameValuePair> request_params = new ArrayList<NameValuePair>();
        request_params.add(new BasicNameValuePair("loginkey", "729090880@qq.com"));
        request_params.add(new BasicNameValuePair("password", "123qwe"));
        request_params.add(new BasicNameValuePair("loginType", "0"));
        httppost.setEntity(new UrlEncodedFormEntity(request_params, "UTF-8"));
        response = httpClient.execute(httppost);
        Assert.assertNotNull(response);
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


    @Test(description = "IP管控",groups = {"smoke","IP"},enabled = true)
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
        urlEncodedFormEntity.setContentEncoding("UTF-8");
        request.setEntity(urlEncodedFormEntity);
        HttpResponse response = httpClient.execute(request);
        //String string = IOUtils
        //logger.debug(string);
        AssertBase.assertCode(response);
        logger.debug(response);
    }




    @Test(description = "二次验证",groups = {"smoke","IP"},enabled = true)
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
        response = httpClient.execute(request);
        Assert.assertEquals(response.getStatusLine().getStatusCode(),200);
        logger.debug(response);
    }
}
