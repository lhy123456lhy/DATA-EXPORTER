package com.ds.tools.util;

import java.io.File;
import java.io.InputStream;

public class FileUtil {

    public static InputStream getResourcesFileInputStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("" + fileName);
    }

    public static String getDataDir(){
        String jdbcPath= FileUtil.class.getClassLoader().getResource("jdbc.properties").getPath();
        String confPath=new File(jdbcPath).getParent();
        String dataPath=new File(confPath).getParent()+File.separator+"data";
        File file=new File(dataPath);
        if(!file.exists()){
            file.mkdir();
        }
        return dataPath;
    }

    public static void main(String[] args) {
        System.out.println(getDataDir());
    }
}
