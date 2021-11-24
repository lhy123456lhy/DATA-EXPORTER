package com.ds.tools.cmd;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ds.tools.model.Log2;
import com.ds.tools.service.Log2Service;
import com.ds.tools.util.SpringContextUtil;

public class LogExportorTest2 extends ExportTemplate {

    private final static Logger _log = LoggerFactory.getLogger(LogExportorTest2.class);

    private Log2Service log2Service;


    @Override
    public void setDBType() {
        this.DBType = 2;
    }

    @Override
    public void setFileNamePre() {
        this.fileNamePre = "log2导出";
    }

    @Override
    public void setEntityClass() {
        this.entityClass = Log2.class;
    }

    @Override
    public void validateArgs(String[] args) {
        //init spring 容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        log2Service = SpringContextUtil.getBean(Log2Service.class);

    }

    @Override
    public List queryList(String[] args, int startOffset) {
        List<Log2> logList = log2Service.getLog2ByOffset(startOffset, 1000);
        return logList;
    }
}
