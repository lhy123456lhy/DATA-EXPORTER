package com.ds.tools.service.impl;

import com.ds.tools.dao.Log2DAO;
import com.ds.tools.model.Log2;
import com.ds.tools.service.Log2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Log2ServiceImpl implements Log2Service {

    @Autowired
    Log2DAO log2DAO;

    @Override
    public List<Log2> getLog2ByOffset(Integer currPage, Integer pageSize) {
        return  log2DAO.getLog2ByOffset(currPage,pageSize);
    }
}
