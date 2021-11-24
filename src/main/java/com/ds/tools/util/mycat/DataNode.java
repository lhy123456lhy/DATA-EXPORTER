package com.ds.tools.util.mycat;

public class DataNode {

    private String name;

    private DataHost dataHost;

    private String dbName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataHost getDataHost() {
        return dataHost;
    }

    public void setDataHost(DataHost dataHost) {
        this.dataHost = dataHost;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
