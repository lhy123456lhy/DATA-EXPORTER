package com.ds.tools.util.mycat;

public class DataHost {
    private String writeHostUrl;
    private String readHostUrl;
    private String name;

    private String userName;
    private String password;

    public String getWriteHostUrl() {
        return writeHostUrl;
    }

    public void setWriteHostUrl(String writeHostUrl) {
        this.writeHostUrl = writeHostUrl;
    }

    public String getReadHostUrl() {
        return readHostUrl;
    }

    public void setReadHostUrl(String readHostUrl) {
        this.readHostUrl = readHostUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
