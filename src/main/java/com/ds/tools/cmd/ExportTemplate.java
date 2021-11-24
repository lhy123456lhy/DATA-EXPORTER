package com.ds.tools.cmd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.ds.tools.datasource.DynamicDataSource;
import com.ds.tools.model.DBoffset;
import com.ds.tools.util.DateUtil;
import com.ds.tools.util.FileUtil;
import com.ds.tools.util.ZipUtil;

public abstract class ExportTemplate{
    private final static Logger _log = LoggerFactory.getLogger(ExportTemplate.class);
    //校验参数
    private final static int MAX_ROW = 1000000;
    Stack<DBoffset> stack;
    int DBType;			//0：公共库，1：log1库；2：log2库
    String fileNamePre;
    Class  entityClass;

    public void init() {
        stack = new Stack<>();
        if (DBType == 0) {
            stack.push(new DBoffset(DynamicDataSource.STANDALONE_DB));
        } else if (DBType == 1) {
        	for (String logDb : DynamicDataSource.LOG1_DB_SET) {
                stack.push(new DBoffset(logDb));
            }
        } else if (DBType == 2) {
            for (String logDb : DynamicDataSource.LOG2_DB_SET) {
                stack.push(new DBoffset(logDb));
            }
        } else {
        	for (String otherDb : DynamicDataSource.DB_SET) {
                stack.push(new DBoffset(otherDb));
            }
        }
    }

    /**
     * 设置导出表DB类型
     */
    public abstract void setDBType();

    /**
     * 设置文件名前缀
     */
    public abstract void setFileNamePre();


    /**
     * 设置实体类
     */
    public abstract void setEntityClass();

    /**
     * 校验传入的参数，并注入bean
     * @param args
     */
    public abstract void validateArgs(String[] args);


    /**
     * 获取列表
     * @param args
     * @return
     */
    public abstract List queryList(String[] args,int startOffset);


    public void exprot(String[] args) {
        validateArgs(args);
        long startTime=System.currentTimeMillis();
        setDBType();
        setFileNamePre();
        setEntityClass();
        init();
        List<File> fileList = new ArrayList<>();
        int fileCount = 1;
        long rowCount = 0;
        boolean needCreatFileFlag = true;
        String fileName = null;
        OutputStream out = null;
        ExcelWriter writer = null;
        Sheet sheet1 = null;
        String dataDir= FileUtil.getDataDir();
        String fileTime = DateUtil.getTime();
        try {
            while (!stack.empty()) {
                DBoffset dBoffset = stack.pop();
                if (needCreatFileFlag) {
                    fileName = fileNamePre + fileTime + "_" + fileCount + ".xlsx";
                    String filePath=dataDir+File.separator+fileName;
                    fileList.add(new File(filePath));
                    out = new FileOutputStream(filePath);
                    writer = EasyExcelFactory.getWriter(out);
                    sheet1 = new Sheet(1, 0, entityClass, "sheet1", null);
                    sheet1.setAutoWidth(Boolean.TRUE);
                }
                DynamicDataSource.setDataSource(dBoffset.getDbName());
                _log.debug("当前数据源:{}", dBoffset.getDbName());
                int startOffset = 0;
                while (true) {
                    //单个sheet最大写入100万
                    if (rowCount > MAX_ROW) {
                        //关闭当前文件，重新压栈
                        writer.finish();
                        out.close();
                        fileCount++;
                        rowCount = 0;
                        needCreatFileFlag = true;
                        dBoffset.setOffset(startOffset);
                        stack.push(dBoffset);
                        break;
                    }

                    //重新压栈的需要跳步走
                    needCreatFileFlag = false;
                    if (dBoffset.getOffset() > 0&&rowCount==0) {
                        startOffset = dBoffset.getOffset();
                    }

                    List logList =queryList(args,startOffset);
                    writer.write(logList, sheet1);
                    rowCount = rowCount + logList.size();
                    if (logList.size() < 1000) {
                        break;
                    }
                    startOffset = startOffset + 1000;
                    _log.info(fileName + "已写行数：{}", rowCount);
                }
            }
            writer.finish();
            out.close();
            //压缩文件
            File file = new File(dataDir+File.separator+fileNamePre + fileTime + ".zip");
            ZipUtil.zipFiles(fileList, file);
            //删除源文件
            for (File fileItem : fileList) {
                fileItem.delete();
            }
            _log.info("导出结束！耗时："+(System.currentTimeMillis()-startTime)/1000/60+"分钟");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
