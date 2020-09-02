package com.filez.zbox.console.Properties;


import java.io.FileInputStream;
import java.util.Properties;

public class PropertyConfig {
    public static final Properties properties = new Properties();
    public static  String getData(String key) {

        String data = null;
        try {
            String path = Thread.currentThread().getContextClassLoader().getResource("./resource/conf/env.properties").getFile();
            properties.load(new FileInputStream(path));
            data = properties.getProperty(key);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return data;
    }
}
