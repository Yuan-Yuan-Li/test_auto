package com.filez.zbox.console.Properties;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyConfig {

    public static final Properties properties = new Properties();

    public static  String getData(String key) {
        String data = null;
        try {
            Config config = ConfigFactory.parseFile(new File("src/resources/env.properties"));
            String env = config.getString("env") + ".properties";
            FileInputStream conf = new FileInputStream("src/resources/conf/"+env);
            properties.load(conf);
            data = properties.getProperty(key);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return data;
    }
}
