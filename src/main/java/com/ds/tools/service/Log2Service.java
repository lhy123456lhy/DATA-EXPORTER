package com.ds.tools.service;

import com.ds.tools.model.Log2;

import java.util.List;

public interface Log2Service {
    public List<Log2> getLog2ByOffset(Integer currPage, Integer pageSize);
}