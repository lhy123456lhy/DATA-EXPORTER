package com.ds.tools.model;

public class DBoffset {
    private String dbName;
    private int offset;

   public DBoffset(String dbName){
        this.dbName=dbName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }


}
