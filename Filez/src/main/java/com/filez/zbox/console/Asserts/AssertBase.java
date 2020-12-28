package com.filez.zbox.console.Asserts;
import org.apache.http.HttpResponse;
import org.testng.Assert;


public class AssertBase {
    public static void  assertCode(HttpResponse response, int expect_status){
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusLine().getStatusCode(),expect_status);
    }
}