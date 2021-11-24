package com.ds.tools.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * 资源文件读取工具
 */
public class PropertiesFileUtil {

    private final static Logger _log = LoggerFactory.getLogger(PropertiesFileUtil.class);

    /**
     * 当打开多个资源文件时，缓存资源文件
     */
    private static HashMap<String, PropertiesFileUtil> configMap = new HashMap<String, PropertiesFileUtil>();
    /**
     *  打开文件时间，判断超时使用
     */
    private Date loadTime = null;
    // 资源文件
    private Properties resourceBundle = null;
    // 默认资源文件名称
    private static final String NAME = "config";
    // 缓存时间
    private static final Integer TIME_OUT = 60 * 1000;


    /**
     * \私有构造方法，创建单例
     * @param filePath
     */
    private PropertiesFileUtil(String filePath) {
        this.loadTime = new Date();
        this.resourceBundle=new Properties();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(filePath));
            resourceBundle.load(in);
        }catch (Exception e){
            e.printStackTrace();
            _log.error(filePath+"加载失败！");
        }
    }

    public static synchronized PropertiesFileUtil getInstance() {
        return getInstance(NAME);
    }

    public static synchronized PropertiesFileUtil getInstance(String name) {
        PropertiesFileUtil conf = configMap.get(name);
        if (null == conf) {
            conf = new PropertiesFileUtil(name);
            configMap.put(name, conf);
        }
        // 判断是否打开的资源文件是否超时1分钟
        if ((System.currentTimeMillis() - conf.getLoadTime().getTime()) > TIME_OUT) {
            conf = new PropertiesFileUtil(name);
            configMap.put(name, conf);
        }
        return conf;
    }


    /**
     * 根据key读取value
     * @param key
     * @return
     */
    public String get(String key) {
        try {
            String value = resourceBundle.getProperty(key);
            return value;
        }catch (MissingResourceException e) {
            return "";
        }
    }

    /**
     * 根据key读取value(整形)
     * @param key
     * @return
     */
    public Integer getInt(String key) {
        try {
            String value = resourceBundle.getProperty(key);
            return Integer.parseInt(value);
        }catch (MissingResourceException e) {
            return null;
        }
    }

    // 根据key读取value(布尔)
    public boolean getBool(String key) {
        try {
            String value = resourceBundle.getProperty(key);
            if ("true".equals(value)) {
                return true;
            }
            return false;
        }catch (MissingResourceException e) {
            return false;
        }
    }

    public Set<String> getKeys(){
        try {
            Set set=resourceBundle.stringPropertyNames();
            return set;
        }catch (MissingResourceException e) {
            return null;
        }
    }

    public Date getLoadTime() {
        return loadTime;
    }

}
