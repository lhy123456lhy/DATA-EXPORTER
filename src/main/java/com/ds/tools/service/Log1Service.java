package com.ds.tools.service;

import java.util.List;

import com.ds.tools.model.Log1;

public interface Log1Service {
    public List<Log1> getLog1ByOffset(Integer offset, Integer limit);
}