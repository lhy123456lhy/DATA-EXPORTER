package com.ds;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ds.tools.datasource.DynamicDataSource;
import com.ds.tools.model.Log1;
import com.ds.tools.service.Log1Service;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ServiceTest {
    private final static Logger _log = LoggerFactory.getLogger(ServiceTest.class);
    @Autowired
    Log1Service log1Service;

    @Test
    public void testGetList() throws IOException {
        DynamicDataSource.setDataSource("log1dn2");
        List<Log1> logList = log1Service.getLog1ByOffset(0, 1000);
        System.out.println(logList);
    }


}
