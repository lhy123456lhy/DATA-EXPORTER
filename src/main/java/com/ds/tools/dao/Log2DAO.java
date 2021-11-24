package com.ds.tools.dao;

import com.ds.tools.model.Log2;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Log2DAO {

	public List<Log2> getLog2ByOffset(@Param("currPage") int currPage, @Param("pageSize") Integer pageSize);
	

	
	
}
