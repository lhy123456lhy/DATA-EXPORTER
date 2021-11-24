package com.ds.tools.cmd;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ds.tools.model.Log1;
import com.ds.tools.service.Log1Service;
import com.ds.tools.util.SpringContextUtil;

public class LogExportorTest1 extends ExportTemplate {

    private final static Logger _log = LoggerFactory.getLogger(LogExportorTest1.class);

    private Log1Service log1Service;


    @Override
    public void setDBType() {
        this.DBType = 1;//设置类型
    }

    @Override
    public void setFileNamePre() {
        this.fileNamePre = "Log1导出";
    }

    @Override
    public void setEntityClass() {
        this.entityClass = Log1.class;
    }

    @Override
    public void validateArgs(String[] args) {
        //init spring 容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        log1Service = SpringContextUtil.getBean(Log1Service.class);
    }

    @Override
    public List queryList(String[] args,int startOffset) {
        Integer days=args.length==2?Integer.valueOf(args[1]):0;
        List<Log1> logList = log1Service.getLog1ByOffset(startOffset, 1000);
        return logList;
    }
}
