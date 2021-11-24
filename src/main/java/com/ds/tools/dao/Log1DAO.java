package com.ds.tools.dao;

import com.ds.tools.model.Log1;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Log1DAO {
    public List<Log1> getLog1ByOffset(@Param("offset") Integer offset, @Param("limit") Integer limit);
}