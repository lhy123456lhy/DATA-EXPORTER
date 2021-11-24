package com.ds.tools.service.impl;

import com.ds.tools.dao.Log1DAO;
import com.ds.tools.model.Log1;
import com.ds.tools.service.Log1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Log1ServiceImpl implements Log1Service {

    @Autowired
    Log1DAO log1DAO;
    @Override
    public List<Log1> getLog1ByOffset(Integer offset, Integer limit) {
        return log1DAO.getLog1ByOffset(offset,limit);
    }
}
