package com.ds.tools.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportMain {
    private final static Logger _log = LoggerFactory.getLogger(ExportMain.class);

    public static void main(String[] args) {

        if (args == null || args.length == 0) {
            System.err.println("请传入日志导出类型：");
            System.err.println("1 log1！");
            System.err.println("2 log2！");
            System.exit(-1);
        }
        String logType = args[0];
        int logTypeI = 0;
        try {
            logTypeI = Integer.valueOf(logType);
        } catch (NumberFormatException e) {
            System.err.println("导出类型错误，请传入有效的日志导出类型！");
            System.exit(-1);
        }
        if (logTypeI < 0) {
            System.err.println("导出类型错误，请传入有效的日志导出类型！");
            System.exit(-1);
        }
        String[] subArgs=new String[args.length-1];
        for(int i=1;i<args.length;i++){
            subArgs[i-1]=args[i];
        }

        switch (logTypeI) {
            case 1:
                System.out.println("开始导出Log1...");
                LogExportorTest1 log1Exportor = new LogExportorTest1();
                log1Exportor.exprot(subArgs);//执行导出，并进行动态数据源的切换
                break;
            case 2:
                System.out.println("开始导出Log2...");
                LogExportorTest2 log2Exportor = new LogExportorTest2();
                log2Exportor.exprot(subArgs);//执行导出，并进行动态数据源的切换
                break;
            default:
                break;
        }


    }
}
